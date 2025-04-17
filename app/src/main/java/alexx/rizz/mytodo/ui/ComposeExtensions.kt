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
    this.ifTrue()
  else
    this.ifFalse()

@Composable
fun <T> rememberUpdatedStateMutable(newValue: T): MutableState<T> =
  rememberUpdatedState(newValue) as MutableState

@Composable @Suppress("unused")
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

val isPreview: Boolean @Composable get() =
  LocalInspectionMode.current

// Может пригодиться
// fun Modifier.selectAllOnFocus(input: MutableState<TextFieldValue>): Modifier =
//   this.onFocusChanged {
//     if (it.isFocused)
//       input.value = input.value.copy(selection = TextRange(0, input.value.text.length))
//   }