package com.kotlin_everywhere.redux.test

import com.kotlin_everywhere.redux.Store
import org.junit.Assert
import org.junit.Test

class SubscribeTest {
    @Test
    fun testSubscribe() {
        data class State(val id: Int = 1)

        val IncreaseId = object {}

        val store = Store(State()) { state, action ->
            when (action) {
                IncreaseId -> state.copy(id = state.id + 1)
                else -> state
            }
        }
        var handlerCalled = 0
        store.subscribe {
            handlerCalled += 1
        }

        Assert.assertEquals(0, handlerCalled)
        store.dispatch(Unit)
        Assert.assertEquals(0, handlerCalled)
        store.dispatch(IncreaseId)
        Assert.assertEquals(1, handlerCalled)
    }

    @Test
    fun testUnSubscribe() {
        data class State(val id: Int = 1)

        val IncreaseId = object {}

        val store = Store(State()) { state, action ->
            when (action) {
                IncreaseId -> state.copy(id = state.id + 1)
                else -> state
            }
        }
        var handlerCalled = 0
        val dismiss = store.subscribe {
            handlerCalled += 1
        }

        Assert.assertEquals(0, handlerCalled)
        store.dispatch(IncreaseId)
        Assert.assertEquals(1, handlerCalled)
        dismiss()
        store.dispatch(IncreaseId)
        Assert.assertEquals(1, handlerCalled)
    }

}