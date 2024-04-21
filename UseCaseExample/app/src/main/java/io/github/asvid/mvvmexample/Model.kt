package io.github.asvid.mvvmexample

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object Model {
    const val MAX_VALUE = 100
    const val MIN_VALUE = 1
    const val DELAY = 1000L

    private var items: List<Int> = emptyList()
    private val _itemsFlow = MutableStateFlow(items)
    val itemsFlow: StateFlow<List<Int>> = _itemsFlow

    fun validateInput(newInput: String?): Result<Int> {
        if (newInput.isNullOrEmpty()) return Result.failure(Exception("input is empty"))
        return if (newInput.all { it.isDigit() }) Result.success(newInput.toInt())
        else Result.failure(Exception("non-digit detected"))
    }

    suspend fun addItem(item: Int): Result<Unit> {
        if (item > MAX_VALUE) return Result.failure(Exception("item bigger than $MAX_VALUE"))
        if (item < MIN_VALUE) return Result.failure(Exception("item smaller than $MIN_VALUE"))
        else {
            delay(DELAY)
            items = items + item
            _itemsFlow.update { items }
            return Result.success(Unit)
        }
    }

    suspend fun removeItem(item: Int): Result<Unit> {
        if (items.find { it == item } == null) return Result.failure(Exception("item not present"))
        else {
            delay(DELAY)
            items = items - item
            _itemsFlow.update { items }
            return Result.success(Unit)
        }
    }
}