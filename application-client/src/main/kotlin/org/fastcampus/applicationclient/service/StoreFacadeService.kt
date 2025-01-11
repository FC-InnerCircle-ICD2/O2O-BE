package org.fastcampus.applicationclient.service

import org.fastcampus.store.service.reader.StoreReaderService
import org.springframework.stereotype.Service

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Service
class StoreFacadeService(
    private val service: StoreReaderService,
) {

}
