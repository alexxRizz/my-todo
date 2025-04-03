package alexx.rizz.mytodo.feature.todolist.ui.components

import androidx.compose.foundation.interaction.*
import androidx.compose.ui.*
import sh.calvin.reorderable.*

fun Modifier.makeDraggable(
  scope: ReorderableCollectionItemScope,
  interactionSource: MutableInteractionSource,
  haptic: ReorderHapticFeedback,
  onDragStopped: () -> Unit
): Modifier =
  with(scope) {
    longPressDraggableHandle(
      onDragStarted = {
        haptic.performHapticFeedback(ReorderHapticFeedbackType.Start)
      },
      onDragStopped = {
        onDragStopped()
        haptic.performHapticFeedback(ReorderHapticFeedbackType.Stop)
      },
      interactionSource = interactionSource
    )
  }
