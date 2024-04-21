package io.github.asvid.mvvmexample

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.asvid.mvvmexample.items.AddItemUseCase
import io.github.asvid.mvvmexample.items.AddItemUseCaseImpl
import io.github.asvid.mvvmexample.items.InMemoryItemsRepository
import io.github.asvid.mvvmexample.items.Item
import io.github.asvid.mvvmexample.items.ReadItemsUseCase
import io.github.asvid.mvvmexample.items.ReadItemsUseCaseImpl
import io.github.asvid.mvvmexample.items.RemoteItemsRepository
import io.github.asvid.mvvmexample.items.RemoveItemUseCase
import io.github.asvid.mvvmexample.items.RemoveItemUseCaseImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class State(
    val items: List<Item> = emptyList(),
    val inputError: String? = null,
    val currentInput: String? = null,
    val isLoading: Boolean = false
)

const val INPUT_SAVED_STATE_KEY = "INPUT_SAVED_STATE_KEY"

class ViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    // this would typically be a singleton injected by DI
    val inMemoryItemsRepository = InMemoryItemsRepository()
    val remoteItemsRepository = RemoteItemsRepository()

    // those would typically be created and injected by DI
    private val readItemsUseCase: ReadItemsUseCase = ReadItemsUseCaseImpl(inMemoryItemsRepository)
    private val removeItemUseCase: RemoveItemUseCase =
        RemoveItemUseCaseImpl(inMemoryItemsRepository)
    // repository is used as interface, so the same use case implementation can handle InMemory/Remote stuff the same way
    private val remoteAddItemUseCase: AddItemUseCase = AddItemUseCaseImpl(remoteItemsRepository)
    private val addItemUseCase: AddItemUseCase = AddItemUseCaseImpl(inMemoryItemsRepository)

    init {
        val inputText = savedStateHandle[INPUT_SAVED_STATE_KEY] ?: ""
        _state.update {
            it.copy(currentInput = inputText)
        }
        viewModelScope.launch {
            readItemsUseCase()
                .onSuccess { itemsFlow ->
                    itemsFlow.collect { itemsUpdate ->
                        _state.update {
                            it.copy(
                                items = itemsUpdate
                            )
                        }
                    }
                }
                .onFailure { error ->
                    displayErrorMessage(error)
                }
        }
    }

    fun addButtonClicked() {
        viewModelScope.launch {
            Model.validateInput(_state.value.currentInput)
                .onSuccess { validatedItem ->
                    showLoading()
                    addItemUseCase(Item(validatedItem))
                        .onSuccess { hideLoading() }
                        .onFailure { error ->
                            hideLoading()
                            displayErrorMessage(error)
                        }
                }
                .onFailure { error ->
                    displayErrorMessage(error)
                }
        }
    }

    private fun hideLoading() {
        _state.update { it.copy(isLoading = false) }
        _state.value = State()
    }

    private fun showLoading() {
        _state.update { it.copy(isLoading = true) }
    }

    fun inputChanged(newInput: String) {
        savedStateHandle[INPUT_SAVED_STATE_KEY] = newInput
        viewModelScope.launch {
            _state.update { it.copy(currentInput = newInput) }
            Model.validateInput(newInput)
                .onSuccess { _state.update { it.copy(inputError = null) } }
                .onFailure { exception -> _state.update { it.copy(inputError = exception.message) } }
        }
    }


    fun removeItem(item: Item) {
        viewModelScope.launch {
            showLoading()
            removeItemUseCase(item)
                .onSuccess { hideLoading() }
                .onFailure { error ->
                    hideLoading()
                    displayErrorMessage(error)
                }
        }
    }

    private fun displayErrorMessage(exception: Throwable) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(inputError = exception.message)
            }
        }
    }

    override fun onCleared() {
        //cancel work that is not handled withing `viewModelScope`
        super.onCleared()
    }
}