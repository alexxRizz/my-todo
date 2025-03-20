package alexx.rizz.mytodo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*

inline fun Modifier.conditional(
  condition: Boolean,
  ifTrue: Modifier.() -> Modifier,
  ifFalse: Modifier.() -> Modifier = { this },
): Modifier =
  if (condition)
    then(ifTrue(Modifier))
  else
    then(ifFalse(Modifier))

@Composable
fun PaddingValues.copy(
  start: Dp = calculateStartPadding(LocalLayoutDirection.current),
  top: Dp = calculateTopPadding(),
  end: Dp = calculateEndPadding(LocalLayoutDirection.current),
  bottom: Dp = calculateBottomPadding(),
) = PaddingValues(
  start = start,
  top = top,
  end = end,
  bottom = bottom
)