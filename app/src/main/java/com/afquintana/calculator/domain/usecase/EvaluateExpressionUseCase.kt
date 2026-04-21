package com.afquintana.calculator.domain.usecase

import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class EvaluateExpressionUseCase @Inject constructor() {

    fun execute(expression: String): String {
        return runCatching { evaluate(expression).stripTrailingZeros().toPlainString() }
            .getOrElse { "Error" }
    }

    private fun evaluate(expression: String): BigDecimal {
        val tokens = tokenize(expression)
        val values = ArrayDeque<BigDecimal>()
        val operators = ArrayDeque<Char>()

        for (token in tokens) {
            when {
                token.length == 1 && token[0] in charArrayOf('+', '-', '×', '÷') -> {
                    while (operators.isNotEmpty() && precedence(operators.last()) >= precedence(token[0])) {
                        applyOperation(values, operators.removeLast())
                    }
                    operators.addLast(token[0])
                }

                else -> values.addLast(token.toBigDecimal())
            }
        }

        while (operators.isNotEmpty()) {
            applyOperation(values, operators.removeLast())
        }

        return values.lastOrNull() ?: BigDecimal.ZERO
    }

    private fun tokenize(expression: String): List<String> {
        val cleaned = expression.replace(" ", "")
        val tokens = mutableListOf<String>()
        val numberBuffer = StringBuilder()

        cleaned.forEachIndexed { index, char ->
            if (char.isDigit() || char == '.') {
                numberBuffer.append(char)
            } else {
                if (numberBuffer.isNotEmpty()) {
                    tokens.add(numberBuffer.toString())
                    numberBuffer.clear()
                }

                if (char == '-' && (index == 0 || cleaned[index - 1] in charArrayOf('+', '-', '×', '÷'))) {
                    numberBuffer.append(char)
                } else {
                    tokens.add(char.toString())
                }
            }
        }

        if (numberBuffer.isNotEmpty()) {
            tokens.add(numberBuffer.toString())
        }

        return tokens
    }

    private fun precedence(operator: Char): Int =
        when (operator) {
            '×', '÷' -> 2
            '+', '-' -> 1
            else -> 0
        }

    private fun applyOperation(values: ArrayDeque<BigDecimal>, operator: Char) {
        val right = values.removeLastOrNull() ?: BigDecimal.ZERO
        val left = values.removeLastOrNull() ?: BigDecimal.ZERO

        val result = when (operator) {
            '+' -> left + right
            '-' -> left - right
            '×' -> left * right
            '÷' -> {
                if (right.compareTo(BigDecimal.ZERO) == 0) {
                    throw ArithmeticException("Division by zero")
                }
                left.divide(right, 10, RoundingMode.HALF_UP)
            }
            else -> BigDecimal.ZERO
        }

        values.addLast(result)
    }

    private operator fun BigDecimal.plus(other: BigDecimal): BigDecimal = add(other)
    private operator fun BigDecimal.minus(other: BigDecimal): BigDecimal = subtract(other)
    private operator fun BigDecimal.times(other: BigDecimal): BigDecimal = multiply(other)
}
