package com.afquintana.calculator.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.afquintana.calculator.presentation.model.CalculatorButton
import androidx.compose.foundation.clickable
import com.afquintana.calculator.presentation.viewmodel.CalculatorViewModel

private val buttons = listOf(
    listOf(CalculatorButton("AC"), CalculatorButton("⌫"), CalculatorButton("÷"), CalculatorButton("×")),
    listOf(CalculatorButton("7"), CalculatorButton("8"), CalculatorButton("9"), CalculatorButton("-")),
    listOf(CalculatorButton("4"), CalculatorButton("5"), CalculatorButton("6"), CalculatorButton("+")),
    listOf(CalculatorButton("1"), CalculatorButton("2"), CalculatorButton("3"), CalculatorButton("=")),
    listOf(CalculatorButton("0", columnSpan = 2), CalculatorButton("."))
)

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = state.result,
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFB0B0B0),
            fontSize = 28.sp,
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = state.expression,
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            fontSize = 58.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 64.sp,
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(20.dp))

        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { button ->
                    CalculatorKey(
                        button = button,
                        onClick = { viewModel.onButtonClick(button.label) },
                        modifier = Modifier.weight(button.columnSpan.toFloat())
                    )
                }
                val missingWeight = 4 - row.sumOf { it.columnSpan }
                if (missingWeight > 0) {
                    Spacer(modifier = Modifier.weight(missingWeight.toFloat()))
                }
            }
        }
    }
}

@Composable
private fun CalculatorKey(
    button: CalculatorButton,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isOperator = button.label in setOf("÷", "×", "-", "+", "=")
    val isTopAction = button.label in setOf("AC", "⌫")

    val backgroundColor = when {
        isOperator -> Color(0xFFFF9500)
        isTopAction -> Color(0xFFA5A5A5)
        else -> Color(0xFF2C2C2E)
    }

    val textColor = if (isTopAction) Color.Black else Color.White

    Box(
        modifier = modifier
            .height(78.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = button.label,
            color = textColor,
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}
