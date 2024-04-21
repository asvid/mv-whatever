package io.github.asvid.mvvmexample.items

import io.github.asvid.mvvmexample.Model.DELAY
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface RemoveItemUseCase {
    suspend operator fun invoke(item: Item): Result<Unit>
}

class RemoveItemUseCaseImpl(
    private val itemsRepository: ItemsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : RemoveItemUseCase {
    override suspend fun invoke(item: Item): Result<Unit> {
        return withContext(defaultDispatcher) {
            // do some checks ASAP
            if (itemsRepository.getItems().find { it == item } == null) {
                Result.failure(
                    DomainError.ItemNotFoundError("Item: $item was not found in repository, it can't be removed")
                )
            } else {
                delay(DELAY)
                // no throwing from UseCase, just nice Results.
                try {
                    // or rely on checks in the Repository
                    itemsRepository.removeItem(item.id)
                    Result.success(Unit)
                } catch (e: Exception) {
                    val domainError = getDomainErrorFromException(e, item)
                    Result.failure(domainError)
                }
            }
        }
    }

    // this is specific for the context of this UseCase
    private fun getDomainErrorFromException(
        e: Exception,
        item: Item
    ) = when (e) {
        is NoSuchElementException -> {
            DomainError.ItemNotFoundError("Item: $item was not found in repository, it can't be removed")
        }
        //
        else -> {
            DomainError.GenericError(e.message ?: "$e")
        }
    }
}
