package io.github.asvid.mviexample.domain

import io.github.asvid.mviexample.redux.BaseStore

class ModelStore : BaseStore<ModelState, ModelAction>(
    initialState = ModelState(),
    reducer = ModelReducer(),
    middlewares = listOf(
        ModelMiddleware()
    )
)