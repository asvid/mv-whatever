package io.github.asvid.mvvmexample.items

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface ReadItemsUseCase {
    suspend operator fun invoke(): Result<Flow<List<Item>>>
}

class ReadItemsUseCaseImpl(
    private val itemsRepository: ItemsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ReadItemsUseCase {
    override suspend fun invoke(): Result<Flow<List<Item>>> {
        return withContext(defaultDispatcher) {
            try {
                // getItemsFlow() will randomly fail, just for fun
                Result.success(itemsRepository.getItemsFlow())
            } catch (e: Exception) {
                Result.failure(DomainError.ErrorWhileReadingItems(e.message ?: "$e"))
            }
        }
    }
}