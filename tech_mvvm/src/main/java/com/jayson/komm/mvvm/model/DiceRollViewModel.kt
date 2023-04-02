package com.jayson.komm.mvvm.model

import androidx.lifecycle.ViewModel
import com.jayson.komm.mvvm.data.DiceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @Author: Jayson
 * @CreateDate: 2023/3/20 20:42
 * @Version: 1.0
 * @Description:
 */
class DiceRollViewModel : ViewModel() {
    // Expose screen UI state
    private val _uiState = MutableStateFlow(DiceUiState())
    val uiState: StateFlow<DiceUiState> = _uiState.asStateFlow()

    init {
        rollDice()
    }

    // Handle business logic
    private fun rollDice() {
        _uiState.value = DiceUiState().copy(
            firstDieValue = 1,
            secondDieValue = 2,
            numberOfRolls = 3,
        )
    }
}