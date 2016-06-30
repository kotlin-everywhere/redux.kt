package com.kotlin_everywhere.redux.test

import com.kotlin_everywhere.redux.Store
import org.junit.Assert
import org.junit.Test

class StoreTest {
    @Test
    fun testCreateStore() {
        data class State(val id: Int = 1)

        val store = Store(State()) { state, action -> state }

        Assert.assertEquals(store.state, State())
    }
}