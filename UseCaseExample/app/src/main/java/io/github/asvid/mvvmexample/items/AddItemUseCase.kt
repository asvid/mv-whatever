package io.github.asvid.mvvmexample.items

import io.github.asvid.mvvmexample.Model.DELAY
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val MAX_VALUE = 100
private const val MIN_VALUE = 1

interface AddItemUseCase {
    suspend operator fun invoke(item: Item): Result<Unit>
}

class AddItemUseCaseImpl(
    private val itemsRepository: ItemsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : AddItemUseCase {
    override suspend fun invoke(item: Item): Result<Unit> {
        return withContext(defaultDispatcher) {
            if (item.value > MAX_VALUE) Result.failure<Unit>(Exception("item bigger than $MAX_VALUE"))
            if (item.value < MIN_VALUE) Result.failure<Unit>(Exception("item smaller than $MIN_VALUE"))
            if (itemsRepository.getItems().find { it == item } != null) {
                Result.failure(
                    DomainError.ItemAlreadyExistsOnTheList("Item: $item already exists")
                )
            } else {
                delay(DELAY)
                // repository is not checking if item exists, no exceptions will be thrown here
                itemsRepository.addItem(item)
                Result.success(Unit)
            }
        }
    }
}