package io.github.asvid.mvvmexample

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Model {
  const val DELAY = 1000L

  private var items: List<Int> = emptyList()
  private val _itemsFlow = MutableStateFlow(items)
  val itemsFlow: StateFlow<List<Int>> = _itemsFlow

  fun validateInput(newInput: String?): Result<Int> {
    if (newInput.isNullOrEmpty()) return Result.failure(Exception("input is empty"))
    return if (newInput.all { it.isDigit() }) Result.success(newInput.toInt())
    else Result.failure(Exception("non-digit detected"))
  }
}
