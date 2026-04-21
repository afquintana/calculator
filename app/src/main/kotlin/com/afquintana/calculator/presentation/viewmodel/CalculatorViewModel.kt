package com.afquintana.calculator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afquintana.calculator.domain.usecase.EvaluateExpressionUseCase
import com.afquintana.calculator.presentation.model.CalculatorUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val evaluateExpressionUseCase: EvaluateExpressionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onButtonClick(label: String) {
        when (label) {
            "AC" -> clearAll()
            "⌫" -> removeLast()
            "=" -> evaluateExpression()
            else -> appendToken(label)
        }
    }

    private fun clearAll() {
        _uiState.value = CalculatorUiState()
    }

    private fun removeLast() {
        _uiState.update { state ->
            val currentExpression = state.expression
            if (currentExpression.length <= 1 || currentExpression == "Error") {
                state.copy(expression = "0", result = "")
            } else {
                state.copy(expression = currentExpression.dropLast(1), result = "")
            }
        }
    }

    private fun appendToken(token: String) {
        _uiState.update { state ->
            val currentExpression = if (state.expression == "0" || state.expression == "Error") {
                ""
            } else {
                state.expression
            }

            state.copy(expression = currentExpression + token)
        }
    }

    private fun evaluateExpression() {
        viewModelScope.launch {
            val expression = _uiState.value.expression
            val result = evaluateExpressionUseCase.execute(expression)
            _uiState.update { state ->
                if (result == "Error") {
                    state.copy(expression = result, result = "")
                } else {
                    state.copy(expression = result, result = expression)
                }
            }
        }
    }
}
