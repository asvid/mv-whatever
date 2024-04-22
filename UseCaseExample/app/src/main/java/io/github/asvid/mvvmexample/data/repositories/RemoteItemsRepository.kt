package io.github.asvid.mvvmexample.data.repositories

import io.github.asvid.mvvmexample.domain.items.Item
import io.github.asvid.mvvmexample.domain.items.repositories.ItemsRepository
import io.github.asvid.mvvmexample.domain.items.services.ItemsService
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID


// remote repository, but auth is not implemented yet
class RemoteItemsRepository(
    private val itemsService: ItemsService
) : ItemsRepository {
    override fun addItem(newItem: Item) {
        itemsService.addItem(newItem)
    }

    override fun removeItem(itemId: UUID) {
        itemsService.removeItem(itemId)
    }

    override fun getItems(): List<Item> {
        return itemsService.getItems()
    }

    override fun getItemsFlow(): StateFlow<List<Item>> {
        throw Exception("not yet possible")
    }
}