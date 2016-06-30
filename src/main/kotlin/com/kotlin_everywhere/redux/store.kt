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

inline fun <T, U, reified V> partial(crossinline getter: (T) -> U, crossinline setter: ((T, U) -> T), crossinline reducer: (U, V) -> U): (T, Any) -> T {
    return { state, action ->
        val partialAction = action as? V
        if (partialAction == null) {
            state
        } else {
            val partialState = getter(state)
            val newPartialState = reducer(partialState, partialAction)
            if (partialState === newPartialState) state else setter(state, newPartialState)
        }
    }
}