package io.github.asvid.mvvmexample.items

sealed class DomainError(
    override val message: String? = null,
) : Throwable() {
    class GenericError(message: String) : DomainError(message)

    class ItemNotFoundError(message: String? = null) : DomainError(message)
    class ItemAlreadyExistsOnTheList(message: String) : DomainError(message)
    class ErrorWhileReadingItems(message: String) : DomainError(message)
}