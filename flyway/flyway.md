## Flyway
`flyway`는 데이터베이스(`RDB`)를 변경하고, 변경 이력을 관리해주는 DB Migration 도구입니다.   
소스코드를 관리하는 `Git`처럼 변경 이력을 버전으로 관리하고, 변경 이력 `history`도 확인할 수 있습니다.

### 도입 배경
담당자 1명이 각 도메인 개발 담당자가 엔티티의 필드를 추가하고 테이블이 변경되는 것을     
추적하는 데 다소 어렵고, 번거로운 작업이 있어서 커뮤니케이션 비용을 줄이고자 flyway 를 도입하게 되었습니다.

### Migration 파일 작성 방법
파일명 아래와 같은 방식으로 명명 규칙을 지정해주시면 됩니다.
```
V<버전 번호>__insert_<테이블 이름>_data.sql
```
- `V` : 버전 번호의 접두사.
- `버전 번호` : 마이그레이션 순서를 나타내는 숫자.
- `.sql`: SQL 파일 확장자.

#### 예시
1. 테이블 생성
```
V1__create_orders_table.sql
V2__create_order_menu_option_table.sql
```
2. 컬럼 추가
```
V3__add_version_column_to_orders.sql
```
3. 컬럼 수정
```
V1__update_price_column_to_orders.sql
```
3. 데이터 삽입 유형
- `초기 데이터 삽입` : 시스템 초기화 시 삽입되는 데이터.
- `샘플 데이터 삽입` : 테스트나 개발 환경에서 사용되는 샘플 데이터.
- `기본 설정 삽입` : 기본 설정 값이나 초기 구성 데이터를 삽입.
- `업데이트 데이터 삽입` : 기존 데이터의 업데이트가 필요한 경우 삽입.
```
V1__insert_initial_data_into_store_table.sql  (초기 데이터 삽입)
V2__insert_sample_data_into_orders_table.sql  (샘플 데이터 삽입)
V3__insert_test_data_for_orders_table.sql  (테스트 데이터 삽입)
V4__insert_default_settings_for_system.sql  (기본 설정 데이터 삽입)
```
4. 목적에 따른 명명 규칙   

여러 테이블을 생성할 때, 여러 컬럼을 업데이트하고 싶을 때 유사한 목적이나 그룹으로 묶어서 파일 이름을 짓습니다.
```
V1__create_orders_related_tables.sql
V2__create_orders_and_order_menu_option_tables.sql
V3__create_system_configuration_tables.sql
```

### 기타 고려 사항
- 데이터의 목적에 따라 파일 이름을 구체적으로 작성하여 나중에 쉽게 추적할 수 있도록 명명 규칙을 지정해주세요.
- 데이터 삽입이 다른 테이블 간의 관계에 영향을 줄 수 있다면, 삽입 순서를 고려해주세요.
- `위의 명명 규칙에 정의되지 않은 예시 이외에 최초 migration 파일을 입력하신다면, 예시와 함께 작성해놔주시면 감사드리겠습니다.`
