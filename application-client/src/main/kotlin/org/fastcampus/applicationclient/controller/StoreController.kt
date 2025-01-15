package org.fastcampus.applicationclient.controller

import org.fastcampus.applicationclient.service.StoreService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by brinst07 on 25. 1. 11..
 */
@RestController
@RequestMapping("/store")
class StoreController(
    private val storeService: StoreService,
) {
    @GetMapping("/category")
    fun getStoresByCategory(category: String) = storeService.getStoresByCategory(category)
}
