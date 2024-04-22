package io.github.asvid.mvvmexample.domain.items.services

import io.github.asvid.mvvmexample.domain.items.Item
import java.util.UUID

interface ItemsService {
    fun addItem(newItem: Item)
    fun removeItem(itemId: UUID)
    fun getItems(): List<Item>
}