package com.afquintana.calculator.presentation.model

data class CalculatorButton(
    val label: String,
    val rowSpan: Int = 1,
    val columnSpan: Int = 1
)
