package io.github.asvid.mviexample.domain

import io.github.asvid.mviexample.redux.Action

sealed class ModelAction : Action {
    data class AddItemAction(val item: Int) : ModelAction()
    data class RemoveItemAction(val item: Int) : ModelAction()
    data class ValidateInputAction(val input: String) : ModelAction()
}