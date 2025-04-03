package alexx.rizz.mytodo.feature.todolist.ui.components

import alexx.rizz.mytodo.feature.todolist.*
import alexx.rizz.mytodo.feature.todolist.TodoListVM.*
import alexx.rizz.mytodo.feature.todolist.ui.TodoListScreenCommon.RowPadding
import alexx.rizz.mytodo.ui.*
import alexx.rizz.mytodo.ui.theme.*
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
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .makeDraggable(this, interactionSource, haptic, onDragStopped),
    interactionSource = interactionSource,
    shape = RoundedCornerShape(5.dp),
    colors = colors,
    onClick = {},
  ) {
    Row(
      Modifier.padding(RowPadding),
      verticalAlignment = Alignment.CenterVertically,
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
      Spacer(Modifier.width(10.dp))
      Text(item.text, fontSize = 18.sp, modifier =
        Modifier
          .conditional(item.isDone, {
            val crossColor = MyColors.UndoneCard
            val strokeWidth = 11f
            drawBehind {
              drawLine(crossColor, Offset(0f, 0f), Offset(size.width, size.height), strokeWidth = strokeWidth)
              drawLine(crossColor, Offset(0f, size.height), Offset(size.width, 0f), strokeWidth = strokeWidth)
            }
          })
      )
      Spacer(Modifier.weight(1f))
      CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        IconButton(onEditClick) { Icon(Icons.Default.Edit, null) }
      }
    }
  }
}