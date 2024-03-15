package com.google.jetfit.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class BaseViewModel<STATE, EFFECT>(initialUiState: STATE): ViewModel() {
    private val _state: MutableStateFlow<STATE> by lazy { MutableStateFlow<STATE>(initialUiState) }
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<EFFECT?>()
    protected val effect = _effect.asSharedFlow()

    protected fun updateState(newState: STATE) {
        _state.update { newState }
    }

    protected fun sendNewEffect(newEffect: EFFECT) {
        viewModelScope.launch { _effect.emit(newEffect) }
    }

    fun<T> tryToExecute(
        call: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                call().also {
                    onSuccess(it)
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }
}