package io.github.asvid.mvvmexample.domain.items.repositories

import io.github.asvid.mvvmexample.domain.items.Item
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

interface ItemsRepository {
    fun addItem(newItem: Item)
    fun removeItem(itemId: UUID)
    fun getItems(): List<Item>
    fun getItemsFlow(): StateFlow<List<Item>>
}