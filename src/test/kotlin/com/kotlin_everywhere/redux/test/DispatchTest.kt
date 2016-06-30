package com.kotlin_everywhere.redux.test

import com.kotlin_everywhere.redux.Store
import org.junit.Assert
import org.junit.Test


class DispatchTest {
    @Test
    fun testDispatch() {
        data class State(val id: Int = 1)

        class UpdateId(val id: Int)

        val store = Store(State()) { state, action ->
            when (action) {
                is UpdateId -> state.copy(id = action.id)
                else -> state
            }
        }

        Assert.assertEquals(1, store.state.id)

        store.dispatch(UpdateId(2))
        Assert.assertEquals(2, store.state.id)

        store(UpdateId(3))
        Assert.assertEquals(3, store.state.id)
    }
}