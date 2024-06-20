package com.google.jetfit.presentation.screens.subscription


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetfit.data.entities.Subscription
import com.google.jetfit.data.repository.instructor.InstructorRepository
import com.google.jetfit.data.util.Result
import com.google.jetfit.data.util.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val instructorRepository: InstructorRepository
) : ViewModel() {

    private val _instructorImageState = MutableStateFlow("")
    val instructorImageState: StateFlow<String> = _instructorImageState.asStateFlow()

    private val _selectedSubscriptionOption = MutableStateFlow(Subscription("", ""))
    val selectedSubscriptionOption: StateFlow<Subscription> =
        _selectedSubscriptionOption.asStateFlow()

    init {
        viewModelScope.launch {
            _instructorImageState.value = instructorRepository.getInstructorImageById(INSTRUCTOR_ID)
        }
    }


    val uiState: StateFlow<SubscriptionUiState> =
        instructorRepository.getSubscriptionOptionsByInstructorId(INSTRUCTOR_ID)
            .asResult()
            .map {
                when (it) {
                    is Result.Loading -> SubscriptionUiState.Loading
                    is Result.Error -> SubscriptionUiState.Error
                    is Result.Success -> {
                        SubscriptionUiState.Ready(
                            subscriptionOptions = it.data
                        ).also { state ->
                            _selectedSubscriptionOption.value = state.subscriptionOptions.first()
                        }
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SubscriptionUiState.Loading,
            )


    fun updateSelectedSubscriptionOption(subscriptionOption: Subscription) {
        _selectedSubscriptionOption.value = subscriptionOption
    }

    companion object {
        private const val INSTRUCTOR_ID = "1"
    }
}