package org.fastcampus.store.mongo.repository

import org.bson.Document
import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreWithDistance
import org.fastcampus.store.exception.StoreException
import org.fastcampus.store.mongo.document.StoreDocument
import org.fastcampus.store.mongo.document.toModel
import org.fastcampus.store.repository.StoreRepository
import org.springframework.data.domain.Sort
import org.springframework.data.geo.Metrics
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.NearQuery
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

@Component
internal class StoreMongoRepositoryCustom(
    private val storeMongoRepository: StoreMongoRepository,
    private val mongoTemplate: MongoTemplate,
) : StoreRepository {
    override fun findByCategory(category: String): List<Store> {
        return storeMongoRepository.findByCategory(category).map { it.toModel() }
    }

    override fun findById(storeId: String): Store? {
        return storeMongoRepository.findById(storeId)
            ?.map { it.toModel() }
            ?.orElse(null)
    }

    override fun findStoreNearByAndCondition(storeId: String, latitude: Double, longitude: Double): StoreWithDistance {
        val pipeline = mutableListOf<AggregationOperation>()

        // 1. GeoNear 단계: 사용자의 좌표를 기준으로 스토어와의 거리를 계산합니다.
        val geoNearOp = buildGeoNearOperation(
            longitude = longitude,
            latitude = latitude,
            maxDistance = 500.0, // 필요에 따라 최대 거리(단위: km)를 조정하세요.
            distanceField = "distance",
            locationField = "location",
        )
        pipeline.add(geoNearOp)

        // 2. 스토어 아이디 기준 필터링: 특정 스토어만 조회합니다.
        pipeline.add(Aggregation.match(Criteria("id").`is`(storeId)))

        // 3. 정렬 및 결과 제한: 거리를 기준으로 오름차순 정렬하고 1개의 결과만 가져옵니다.
        pipeline.add(Aggregation.sort(Sort.by(Sort.Order.asc("distance"))))
        pipeline.add(Aggregation.limit(1))

        // 4. Aggregation 실행
        val aggregation = Aggregation.newAggregation(pipeline)
        val result = mongoTemplate.aggregate(aggregation, "stores", StoreDocument::class.java).mappedResults.firstOrNull()
            ?: throw RuntimeException("Store with id $storeId not found")

        // 5. 결과를 StoreWithDistance 객체로 변환하여 반환합니다.
        return StoreWithDistance(
            store = result.toModel(),
            distance = (result.distance ?: 0.0).toString(), // 필요에 따라 Double이나 포맷팅된 String으로 반환 가능
        )
    }

    override fun findOwnerIdByStoreId(storeId: String): String? {
        val query = Query(Criteria.where("id").`is`(storeId))
            .also { it.fields().include("ownerId").exclude("_id") }

        val result = mongoTemplate.findOne(query, OwnerIdProjection::class.java, "stores")
        return result?.ownerId
    }

    override fun findByOwnerId(ownerId: String): String {
        val store = storeMongoRepository.findByOwnerId(ownerId) ?: throw StoreException.StoreNotFoundException(ownerId)
        return store.id!!
    }

    override fun findStoreNearbyAndCondition(
        latitude: Double,
        longitude: Double,
        category: Store.Category?,
        searchName: String?,
        page: Int,
        size: Int,
    ): Pair<List<StoreWithDistance>, Int?> { // 결과와 nextCursor 반환
        val nearQuery = NearQuery.near(longitude, latitude)
            .spherical(true)
            .maxDistance(500.0, Metrics.KILOMETERS)

        val query = Query()

        category?.let {
            query.addCriteria(Criteria.where("category").`is`(it.code))
        }

        searchName?.let {
            query.addCriteria(Criteria.where("name").regex(".*$it.*", "i"))
        }

        val skip = page * size
        query.skip(skip.toLong())
        query.limit(size + 1) // 요청 크기보다 하나 더 가져옴 (다음 페이지 확인용)

        nearQuery.query(query)

        val geoResults = mongoTemplate.geoNear(nearQuery, StoreDocument::class.java)

        val stores = geoResults.map { StoreWithDistance(it.content.toModel(), it.distance.value.toString()) }

        val nextCursor = if (stores.size > size) {
            val lastStore = stores.last()
            lastStore.store.id?.toInt()
        } else {
            null
        }

        val content = stores.take(size) // 실제 반환할 데이터

        return Pair(content, nextCursor)
    }

    override fun findStoreNearByAndConditionWithCursor(
        latitude: Double,
        longitude: Double,
        category: Store.Category?,
        searchName: String?,
        cursorDistance: Double?,
        cursorStoreId: String?,
        size: Int,
    ): Pair<List<StoreWithDistance>, String?> {
        val pipeline = mutableListOf<AggregationOperation>()

        // 1) getNear 를 통해서 가져오기
        val geoNearOp = buildGeoNearOperation(
            longitude = longitude,
            latitude = latitude,
            maxDistance = 500.0,
            distanceField = "distance",
            locationField = "location",
        )

        pipeline.add(geoNearOp)

        if (category != null) {
            val dbCategoryString = convertToCategory(category)
            pipeline.add(Aggregation.match(Criteria("category").`is`(dbCategoryString)))
        }

        if (!searchName.isNullOrBlank()) {
            pipeline.add(Aggregation.match(Criteria("name").regex(".*$searchName.*", "i")))
        }

        // 커서 매칭
        if (cursorDistance != null && cursorStoreId != null) {
            val c1 = Criteria("distance").gt(cursorDistance)
            val c2 = Criteria().andOperator(
                Criteria("distance").`is`(cursorDistance),
                Criteria("id").gt(cursorStoreId),
            )
            pipeline.add(Aggregation.match(Criteria().orOperator(c1, c2)))
        }

        pipeline.add(
            Aggregation.sort(
                Sort.by(
                    Sort.Order.asc("distance"),
                    Sort.Order.asc("id"),
                ),
            ),
        )

        pipeline.add(Aggregation.limit(size.toLong() + 1))

        val agg = Aggregation.newAggregation(pipeline)
        val results = mongoTemplate.aggregate(agg, "stores", StoreDocument::class.java).mappedResults

        // 1) 다음 페이지 존재 여부(hasNext) 확인
        // size+1개를 가져왔기 때문에, 실제로 size보다 큰지 보면 됨
        val hasNext = (results.size > size)

        // 2) 실제 반환할 데이터(content)는 size개까지만
        val content = results.take(size)

        // 3) nextCursor 생성
        //    만약 size+1번째 데이터가 있다면(즉 hasNext=true),
        //    그 문서의 distance / id 등을 합쳐서 cursor 문자열로 만든다
        val nextCursor = if (hasNext) {
            val last = results[size] // 0-based index → size번째 = size+1번째 문서
            // distance 와 id가 둘 다 null이 아닐 거라 가정
            "${last.distance}_${last.id}"
        } else {
            null
        }

        // 4) 각 문서를 우리가 쓰는 모델(Store)로 변환 + distance를 함께 담을 수도 있음
        val storeWithDistances = content.map { doc ->
            // doc.distance 에는 Double?가 들어있다고 가정
            StoreWithDistance(
                store = doc.toModel(),
                distance = (doc.distance ?: 0.0).toString(),
            )
        }

// 5) 페이징에 필요한 (결과 목록, nextCursor) 구조로 반환
        return Pair(storeWithDistances, nextCursor)
    }

    override fun updateOrderCnt(storeId: String, orderCnt: Int) {
        val query = Query(Criteria.where("id").`is`(storeId))
        val update = Update().set("orderCount", orderCnt)
        mongoTemplate.updateFirst(query, update, StoreDocument::class.java)
    }

    override fun existsStoreNearBy(storeId: String, latitude: Double, longitude: Double, distanceKM: Double): Boolean {
        val pipeline = mutableListOf<AggregationOperation>()

        // 1. GeoNear 단계: 사용자의 좌표를 기준으로 스토어와의 거리를 계산합니다.
        val geoNearOp = buildGeoNearOperation(
            longitude = longitude,
            latitude = latitude,
            maxDistance = distanceKM,
            distanceField = "distance",
            locationField = "location",
        )
        pipeline.add(geoNearOp)

        // 2. 스토어 아이디 기준 필터링: 특정 스토어만 조회합니다.
        pipeline.add(Aggregation.match(Criteria("id").`is`(storeId)))

        // 3. 1개의 결과만 가져옵니다.
        pipeline.add(Aggregation.limit(1))

        // 4. 개수 확인
        val countOperation = Aggregation.count().`as`("count")
        pipeline.add(countOperation)

        // 5. Aggregation 실행
        val aggregation = Aggregation.newAggregation(pipeline)
        val result = mongoTemplate.aggregate(
            aggregation,
            "stores",
            Document::class.java,
        ).mappedResults.firstOrNull()

        return (result != null && result.getInteger("count") > 0)
    }

    private fun convertToCategory(category: Store.Category): String {
        val dbCategoryString = when (category) {
            Store.Category.CAFE -> "CAFE"
            Store.Category.KOREAN_CUISINE -> "KOREAN_CUISINE"
            Store.Category.JAPANESE_CUISINE -> "JAPANESE_CUISINE"
            Store.Category.BBQ -> "BBQ"
            Store.Category.SALAD -> "SALAD"
            Store.Category.LUNCH_BOX -> "LUNCH_BOX"
            Store.Category.ASIAN_CUISINE -> "ASIAN_CUISINE"
            Store.Category.CHINESE_CUISINE -> "CHINESE_CUISINE"
            Store.Category.CHICKEN -> "CHICKEN"
            Store.Category.SNACK_FOOD -> "SNACK_FOOD"
            Store.Category.PIZZA_WESTERN -> "PIZZA_WESTERN"
            Store.Category.BURGER -> "BURGER"
            Store.Category.KOREAN_STEW -> "KOREAN_STEW"
            Store.Category.SUSHI -> "SUSHI"
            Store.Category.PORK_DISHES -> "PORK_DISHES"
            Store.Category.SANDWICH -> "SANDWICH"
            Store.Category.ETC -> "ETC"
        }
        return dbCategoryString
    }
}

private fun buildGeoNearOperation(
    longitude: Double,
    latitude: Double,
    maxDistance: Double = 500.0,
    distanceField: String = "distance",
    locationField: String = "location",
): AggregationOperation {
    val geoNearStageDoc = Document(
        "\$geoNear",
        Document().apply {
            put(
                "near",
                Document().apply {
                    put("type", "Point")
                    put("coordinates", listOf(longitude, latitude))
                },
            )
            put("distanceField", distanceField)
            put("spherical", true)
            put("maxDistance", maxDistance * 1000)
            put("key", locationField)
        },
    )
    return AggregationOperation { geoNearStageDoc }
}

internal data class OwnerIdProjection(
    val ownerId: String,
)
