package io.github.asvid.mvvmexample.domain.items.usecases

import io.github.asvid.mvvmexample.domain.errors.DomainError
import io.github.asvid.mvvmexample.domain.items.Item
import io.github.asvid.mvvmexample.domain.items.repositories.ItemsRepository
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
                delay(1000L)
                // no throwing from UseCase, just nice Results.
                try {
                    // or rely on checks in the Repository
                    itemsRepository.removeItem(item.id)
                    Result.success(Unit)
                } catch (e: Exception) {
                    // catching generic exception is usually a code smell,
                    // but here I don't want any exception to pass further
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
