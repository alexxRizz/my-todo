package alexx.rizz.mytodo.feature.todolist.ui.components

import alexx.rizz.mytodo.feature.todolist.*
import alexx.rizz.mytodo.feature.todolist.TodoListVM.*
import alexx.rizz.mytodo.feature.todolist.ui.TodoListScreenCommon.RowPadding
import alexx.rizz.mytodo.ui.*
import alexx.rizz.mytodo.ui.theme.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*
import sh.calvin.reorderable.*

@Composable
fun TodoItems(
  modifier: Modifier,
  items: List<TodoItem>,
  onDoneClick: (TodoItemId, Boolean) -> Unit,
  onEditClick: (TodoItemId) -> Unit,
  onDragStopped: (UserIntent.ReorderItems) -> Unit
) {
  val haptic = rememberReorderHapticFeedback()
  var reordered by rememberUpdatedStateMutable(items)
  val lazyListState = rememberLazyListState()
  val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
    reordered = reordered.toMutableList().apply {
      add(to.index, removeAt(from.index))
    }
    haptic.performHapticFeedback(ReorderHapticFeedbackType.Move)
  }
  LazyColumn(
    modifier,
    state = lazyListState,
    verticalArrangement = Arrangement.spacedBy(7.dp)
  ) {
    items(reordered, key = { it.id.asInt }) { item ->
      ReorderableItem(reorderableLazyListState, item.id.asInt) {
        ItemRow(item, haptic,
          onDoneClick = { isDone -> onDoneClick(item.id, isDone) },
          onEditClick = { onEditClick(item.id) },
          onDragStopped = { onDragStopped(UserIntent.ReorderItems(reordered)) }
        )
      }
    }
  }
}

@Composable
private fun ReorderableCollectionItemScope.ItemRow(
  item: TodoItem,
  haptic: ReorderHapticFeedback,
  onDoneClick: (Boolean) -> Unit,
  onEditClick: () -> Unit,
  onDragStopped: () -> Unit,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val colors = CardDefaults.cardColors(
    containerColor = if (item.isDone) MyColors.DoneCard else MyColors.UndoneCard,
  )
  val animatedCrossLine = rememberAnimatedCrossState()
  val animatedCrossLinesScope = rememberCoroutineScope()
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .makeDraggable(this, interactionSource, haptic, onDragStopped)
      .conditional(item.isDone, { drawCross(animatedCrossLine.line1.value, animatedCrossLine.line2.value) }),
    interactionSource = interactionSource,
    shape = RoundedCornerShape(5.dp),
    colors = colors,
    onClick = {},
  ) {
    Row(
      Modifier.padding(RowPadding),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      DoneCheckBox(item, onDoneClick = {
        onDoneClick(it)
        if (it)
          animatedCrossLinesScope.launchCrossAnimation(animatedCrossLine)
      })
      Spacer(Modifier.width(10.dp))
      ItemText(item.text, item.isDone)
      EditButton(onEditClick, item)
    }
  }
}

@Composable
private fun DoneCheckBox(
  item: TodoItem,
  onDoneClick: (Boolean) -> Unit,
) {
  CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 20.dp) {
    Checkbox(
      item.isDone,
      colors = CheckboxDefaults.colors(checkedColor = MyColors.UndoneCard),
      modifier = Modifier
        .scale(1.5f)
        .padding(vertical = 5.dp),
      onCheckedChange = onDoneClick,
    )
  }
}

@Composable
private fun RowScope.ItemText(text: String, isDone: Boolean) {
  Text(
    text,
    fontSize = 18.sp,
    modifier = Modifier.weight(1f),
    color = if (isDone) LocalContentColor.current.copy(alpha = 0.7f) else LocalContentColor.current
  )
}

@Composable
private fun EditButton(onEditClick: () -> Unit, item: TodoItem) {
  CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
    IconButton(onEditClick) {
      val editIcon = Icons.Default.Edit
      val tint = editIcon.tintColor.copy(alpha = if (item.isDone) 0.35f else 1f)
      Icon(editIcon, null, tint = tint)
    }
  }
}

private class AnimatedCrossState(
  val line1: Animatable<Float, AnimationVector1D> = Animatable(1f),
  val line2: Animatable<Float, AnimationVector1D> = Animatable(1f)
)

@Composable
private fun rememberAnimatedCrossState(): AnimatedCrossState =
  remember { AnimatedCrossState() }

@Composable
private fun Modifier.drawCross(animatedLine1: Float, animatedLine2: Float): Modifier =
  drawBehind {
    val xLeft = 83f
    val yTop = 10f
    val xRight = size.width - 20f
    val yBottom = size.height - yTop
    val crossColor = MyColors.UndoneCard
    val strokeWidth = 5f
    drawLine(
      crossColor,
      Offset(xLeft, yTop),
      Offset(
        xLeft - (xLeft - xRight) * animatedLine1,
        yTop - (yTop - yBottom) * animatedLine1
      ),
      strokeWidth,
    )
    drawLine(
      crossColor,
      Offset(xRight, yTop),
      Offset(
        xRight - (xRight - xLeft) * animatedLine2,
        yTop - (yTop - yBottom) * animatedLine2
      ),
      strokeWidth,
    )
  }

private fun CoroutineScope.launchCrossAnimation(animatedCrossState: AnimatedCrossState) {
  this.launch {
    val targetValue = 1f
    val durationMillis = 250
    animatedCrossState.line1.snapTo(0f)
    animatedCrossState.line2.snapTo(0f)
    animatedCrossState.line1.animateTo(
      targetValue = targetValue,
      animationSpec = tween(durationMillis = durationMillis)
    )
    animatedCrossState.line2.animateTo(
      targetValue = targetValue,
      animationSpec = tween(durationMillis = durationMillis)
    )
  }
}