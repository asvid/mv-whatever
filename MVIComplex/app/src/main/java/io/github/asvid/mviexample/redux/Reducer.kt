package io.github.asvid.mviexample.redux

interface Reducer<S : State, A : Action> {
    fun reduce(currentState: S, action: A): S
}