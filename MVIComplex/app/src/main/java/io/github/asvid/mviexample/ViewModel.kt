package io.github.asvid.mviexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.asvid.mviexample.domain.Model

data class State(
    val items: List<Int> = emptyList(),
    val inputError: String? = null,
    val currentInput: String? = null,
    val isLoading: Boolean = false
)

sealed class SideEffect {
    class ShowError(val text: String) : SideEffect()
    class ShowToast(val text: String) : SideEffect()
}

sealed class Event {
    data object AddButtonClicked : Event()
    class InputChanged(val input: String) : Event()
    class RemoveItem(val item: Int) : Event()
}

class ViewModel : ViewModel() {

    val container = Container<State, SideEffect>(viewModelScope, State())

    init {
        container.intent {
            Model.itemsFlow.collect { itemsUpdate ->
                reduce {
                    copy(
                        items = itemsUpdate
                    )
                }
            }
        }
    }

    fun handleEvent(event: Event) {
        when (event) {
            Event.AddButtonClicked -> addButtonClicked()
            is Event.InputChanged -> inputChanged(event.input)
            is Event.RemoveItem -> removeItem(event.item)
        }
    }

    private fun addButtonClicked() = container.intent {
        Model.validateInput(container.state.value.currentInput)
            .onSuccess { validatedItem ->
                showLoading()
                Model.addItem(validatedItem)
                    .onSuccess { hideLoading() }
                    .onFailure { error ->
                        hideLoading()
                        postSideEffect(SideEffect.ShowError(error.message.orEmpty()))
                    }
            }
            .onFailure { error ->
                postSideEffect(SideEffect.ShowError(error.message.orEmpty()))
            }
    }

    private suspend fun hideLoading() {
        container.reduce {
            copy(isLoading = false)
        }
    }

    private suspend fun showLoading() {
        container.reduce {
            copy(isLoading = true)
        }
    }

    private fun inputChanged(newInput: String) = container.intent {
        container.reduce { copy(currentInput = newInput) }
        Model.validateInput(newInput)
            .onSuccess { reduce { copy(inputError = null) } }
            .onFailure { exception -> reduce { copy(inputError = exception.message) } }
    }


    private fun removeItem(item: Int) = container.intent {
        showLoading()
        Model.removeItem(item)
            .onSuccess { hideLoading() }
            .onFailure { error ->
                hideLoading()
                container.postSideEffect(SideEffect.ShowError(error.message.orEmpty()))
            }
    }
}