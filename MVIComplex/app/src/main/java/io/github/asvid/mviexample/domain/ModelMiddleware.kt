package io.github.asvid.mviexample.domain

import io.github.asvid.mviexample.redux.Middleware
import io.github.asvid.mviexample.redux.Store

class ModelMiddleware: Middleware<ModelState, ModelAction> {
    override suspend fun process(
        action: ModelAction,
        currentState: ModelState,
        store: Store<ModelState, ModelAction>
    ) {
        TODO("Not yet implemented")
    }
}