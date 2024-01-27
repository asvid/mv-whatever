package io.github.asvid.mviexample.domain

import io.github.asvid.mviexample.redux.State
// this is in UI package, so... Domain depends on UI? seems wrong.
// this example is kinda wrong itself, there are multiple stores, like per-VM
// whats the point of doing it instead of having VM state?
class ModelState(
    val items: List<Int> = emptyList(),
    val inputError: String? = null,
    val currentInput: String? = null,
    val isLoading: Boolean = false
): State