package com.kotlin_everywhere.redux

class Store<T>(initialState: T, private val reducer: (T, Any) -> T) {
    @Volatile var state: T = initialState
        private set

    fun dispatch(action: Any) {
        state = reducer(state, action)
    }
}

