package io.github.asvid.mvpexample

import kotlinx.coroutines.launch

class MainViewPresenter : Contract.ConcretePresenter, BasePresenter<Contract.ConcreteView>() {

    private var currentInput: String? = null

    override fun onViewAttached() {
        launch {
            Model.itemsFlow.collect { items ->
                view.displayItems(items)
            }
        }
    }

    override fun onViewDetached() {
        // NOOP
    }

    override fun addButtonClicked() {
        launch {
            Model.validateInput(currentInput)
                .onSuccess { validatedItem ->
                    view.displayLoading()
                    Model.addItem(validatedItem)
                        .onFailure { error ->
                            view.hideLoading()
                            displayErrorMessage(error)
                        }.onSuccess {
                            view.hideLoading()
                        }
                }
                .onFailure { error ->
                    displayErrorMessage(error)
                }
        }
    }

    override fun inputChanged(newInput: String) {
        currentInput = newInput
        Model.validateInput(newInput)
            .onFailure { error ->
                displayErrorMessage(error)
            }.onSuccess {
                view.hideError()
            }
    }

    override fun removeItem(item: Int) {
        launch {
            view.displayLoading()
            Model.removeItem(item)
                .onFailure { error ->
                    view.hideLoading()
                    displayErrorMessage(error)
                }
                .onSuccess {
                    view.hideLoading()
                }
        }
    }

    private fun displayErrorMessage(exception: Throwable) {
        view.displayError(exception.message.orEmpty())
    }
}