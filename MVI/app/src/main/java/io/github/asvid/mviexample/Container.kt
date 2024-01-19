@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.asvid.mviexample

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

class Container<STATE, SIDE_EFFECT>(
    private val scope: CoroutineScope,
    initialState: STATE
) {

    private val _state: MutableStateFlow<STATE> = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state

    private val _sideEffect = Channel<SIDE_EFFECT>(Channel.BUFFERED)
    val sideEffect: Flow<SIDE_EFFECT> = _sideEffect.receiveAsFlow()


    suspend fun reduce(reducer: STATE.() -> STATE) {
        withContext(SINGLE_THREAD) {
            _state.value = _state.value.reducer()
        }
    }

    fun intent(transform: suspend Container<STATE, SIDE_EFFECT>.() -> Unit) {
        scope.launch(SINGLE_THREAD) { // to make handling intents non-blocking
            this@Container.transform()
        }
    }

    suspend fun postSideEffect(sideEffect: SIDE_EFFECT) {
        _sideEffect.send(sideEffect)
    }

    companion object {
        private val SINGLE_THREAD = newSingleThreadContext("mvi")
    }
}