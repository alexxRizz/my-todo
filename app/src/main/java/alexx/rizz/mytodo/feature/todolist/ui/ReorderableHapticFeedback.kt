package alexx.rizz.mytodo.feature.todolist.ui

import android.view.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.core.view.*

enum class ReorderHapticFeedbackType { Start, Move, Stop }

class ReorderHapticFeedback(private val view: View) {

  fun performHapticFeedback(type: ReorderHapticFeedbackType) {
    when (type) {
      ReorderHapticFeedbackType.Start ->
        ViewCompat.performHapticFeedback(
          view,
          HapticFeedbackConstantsCompat.GESTURE_START
        )
      ReorderHapticFeedbackType.Move ->
        ViewCompat.performHapticFeedback(
          view,
          HapticFeedbackConstantsCompat.SEGMENT_FREQUENT_TICK
        )
      ReorderHapticFeedbackType.Stop ->
        ViewCompat.performHapticFeedback(
          view,
          HapticFeedbackConstantsCompat.GESTURE_END
        )
    }
  }
}

@Composable
fun rememberReorderHapticFeedback(): ReorderHapticFeedback {
  val view = LocalView.current
  return remember { ReorderHapticFeedback(view) }
}