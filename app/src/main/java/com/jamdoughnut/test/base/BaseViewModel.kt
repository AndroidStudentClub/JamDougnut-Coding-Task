package com.jamdoughnut.test.base

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<S : Any>(initialState: S) : ViewModel() {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)

    val state: StateFlow<S> get() = _state

    @MainThread
    protected fun setState(reducer: S.() -> S) {
        val currentState = _state.value
        val newState = currentState.reducer()
        if (newState != currentState) {
            _state.value = newState
        }
    }

    override fun onCleared() {
        super.onCleared()
        // TODO add some logic if needed

    }
}
