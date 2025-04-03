package alexx.rizz.mytodo.ui

import androidx.compose.runtime.*
import androidx.compose.ui.*

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
fun <T> rememberUpdatedStateMutable(newValue: T): MutableState<T> =
  rememberUpdatedState(newValue) as MutableState

// Может пригодиться
// @Composable
// fun PaddingValues.copy(
//   start: Dp = calculateStartPadding(LocalLayoutDirection.current),
//   top: Dp = calculateTopPadding(),
//   end: Dp = calculateEndPadding(LocalLayoutDirection.current),
//   bottom: Dp = calculateBottomPadding(),
// ) = PaddingValues(
//   start = start,
//   top = top,
//   end = end,
//   bottom = bottom
// )

// Может пригодиться
// fun Modifier.selectAllOnFocus(input: MutableState<TextFieldValue>): Modifier =
//   this.onFocusChanged {
//     if (it.isFocused)
//       input.value = input.value.copy(selection = TextRange(0, input.value.text.length))
//   }