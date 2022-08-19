package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun DrawDonat(vararg colors: Pair<Color, Int>, modifier: Modifier=Modifier) {
    val sum = colors.sumOf { it.second }
    val proportions = colors.map {
        it.first to it.second * 100f / sum
    }
    val sweepAngles = proportions.map {
        it.first to 360 * it.second / 100
    }
    var startAngle by remember {
        mutableStateOf(270f)
    }
    Column {
        Canvas(
            modifier = modifier
        ) {
            sweepAngles.forEach {
                drawArc(
                    color = it.first,
                    startAngle = startAngle,
                    sweepAngle = it.second,
                    useCenter = false,
                    style = Stroke(20f),
                    size = size
                )
                startAngle += it.second
            }
        }

    }
}