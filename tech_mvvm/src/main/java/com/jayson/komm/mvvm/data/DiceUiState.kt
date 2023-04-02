package com.jayson.komm.mvvm.data

/**
 * @Author: Jayson
 * @CreateDate: 2023/3/20 20:40
 * @Version: 1.0
 * @Description:
 */
data class DiceUiState(
    val firstDieValue: Int? = null,
    val secondDieValue: Int? = null,
    val numberOfRolls: Int = 0
)
