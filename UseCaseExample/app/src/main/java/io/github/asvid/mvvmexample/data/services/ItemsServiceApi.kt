package io.github.asvid.mvvmexample.data.services

import io.github.asvid.mvvmexample.data.ApiClient
import io.github.asvid.mvvmexample.domain.items.Item
import io.github.asvid.mvvmexample.domain.items.services.ItemsService
import java.util.UUID

class ItemsServiceApi(apiClient: ApiClient) : ItemsService {
    override fun addItem(newItem: Item) {
        TODO("Not yet implemented")
    }

    override fun removeItem(itemId: UUID) {
        TODO("Not yet implemented")
    }

    override fun getItems(): List<Item> {
        TODO("Not yet implemented")
    }
}