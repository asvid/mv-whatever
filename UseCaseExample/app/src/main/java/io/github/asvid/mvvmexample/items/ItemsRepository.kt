package io.github.asvid.mvvmexample.items

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Random
import java.util.UUID

interface ItemsRepository {
    fun addItem(newItem: Item)
    fun removeItem(itemId: UUID)
    fun getItems(): List<Item>
    fun getItemsFlow(): StateFlow<List<Item>>
}

class InMemoryItemsRepository : ItemsRepository {
    private var items: MutableList<Item> = mutableListOf()
    private val _itemsFlow = MutableStateFlow(items)

    override fun addItem(newItem: Item) {
        // no check here, overwrite if item exists
        items.add(newItem)
        updateFlow()
    }

    override fun removeItem(itemId: UUID) {
        // do yourself a favour and don't use `it`, name the element
        val itemToRemove = items.find { item -> item.id == itemId }
        if (itemToRemove == null) throw NoSuchElementException()
        else items.remove(itemToRemove)
        updateFlow()
    }

    override fun getItems(): List<Item> {
        return items
    }

    override fun getItemsFlow(): StateFlow<List<Item>> {
        if (Random().nextBoolean()) throw Exception("Oh no, I failed")
        return _itemsFlow.asStateFlow()
    }

    private fun updateFlow() {
        _itemsFlow.update { items }
    }
}

// remote repository, but auth is not implemented yet
class RemoteItemsRepository : ItemsRepository {
    override fun addItem(newItem: Item) {
        throw SecurityException("user unauthorized")
    }

    override fun removeItem(itemId: UUID) {
        throw SecurityException("user unauthorized")
    }

    override fun getItems(): List<Item> {
        throw SecurityException("user unauthorized")
    }

    override fun getItemsFlow(): StateFlow<List<Item>> {
        throw SecurityException("user unauthorized")
    }
}