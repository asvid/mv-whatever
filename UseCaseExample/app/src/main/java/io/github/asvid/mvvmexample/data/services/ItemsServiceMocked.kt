package io.github.asvid.mvvmexample.data.services

import io.github.asvid.mvvmexample.domain.items.Item
import io.github.asvid.mvvmexample.domain.items.services.ItemsService
import java.util.UUID

class ItemsServiceMocked: ItemsService {
    override fun addItem(newItem: Item) {
        // do nothing
    }

    override fun removeItem(itemId: UUID) {
        // do nothing
    }

    override fun getItems(): List<Item> {
        // do nothing
        return emptyList()
    }
}