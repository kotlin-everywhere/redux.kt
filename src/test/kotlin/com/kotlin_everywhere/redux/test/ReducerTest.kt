package com.kotlin_everywhere.redux.test

import com.kotlin_everywhere.redux.Store
import org.junit.Assert
import org.junit.Test

private sealed class HeadAction {
    class SetId(val id: Int) : HeadAction()
}

private fun headActionReducer(state: PartialState.Head, action: HeadAction): PartialState.Head = when (action) {
    is HeadAction.SetId -> if (state.id === action.id) state else state.copy(id = action.id)
}

private data class PartialState(val head: Head = PartialState.Head(), val tail: Tail = PartialState.Tail()) {
    data class Head(val id: Int = 0)
    data class Tail(val id: Int = 0)
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

class ReducerTest {
    @Test
    fun testPartial() {
        val headReducer = partial({ state: PartialState -> state.head }, { state, head -> state.copy(head = head) }, ::headActionReducer)

        val store = Store(PartialState(), headReducer)

        // check initial state
        Assert.assertEquals(0, store.state.head.id)
        Assert.assertEquals(0, store.state.tail.id)

        // dispatch non relative action and data should be remain same.
        store.dispatch(Unit)
        Assert.assertEquals(0, store.state.head.id)
        Assert.assertEquals(0, store.state.tail.id)

        store.dispatch(HeadAction.SetId(1))
        Assert.assertEquals(1, store.state.head.id)
        Assert.assertEquals(0, store.state.tail.id)
    }
}

