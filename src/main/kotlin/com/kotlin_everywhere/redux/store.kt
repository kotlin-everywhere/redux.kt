package com.kotlin_everywhere.redux

class Store<T>(initialState: T, private val reducer: (T, Any) -> T) {
    @Volatile var state: T = initialState
        private set

    @Volatile private var handlers: Array<() -> Unit> = arrayOf()

    fun dispatch(action: Any) {
        val previousState = state;
        state = reducer(state, action)
        if (previousState !== state) {
            handlers.forEach { it() }
        }
    }

    fun subscribe(handler: () -> Unit): () -> Unit {
        handlers += handler
        return {
            handlers = handlers.filter { it !== handler }.toTypedArray()
        }
    }
}

