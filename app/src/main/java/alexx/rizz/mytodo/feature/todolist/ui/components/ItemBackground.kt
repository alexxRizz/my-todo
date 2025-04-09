package alexx.rizz.mytodo.feature.todolist.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.shape.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

fun Modifier.drawBackground(verticalGradientColors: List<Color>): Modifier =
  this.background(
    Brush.verticalGradient(verticalGradientColors),
    RoundedCornerShape(7.dp)
  )