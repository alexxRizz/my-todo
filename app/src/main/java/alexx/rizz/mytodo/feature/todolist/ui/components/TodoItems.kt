package alexx.rizz.mytodo.feature.todolist.ui.components

import alexx.rizz.mytodo.feature.todolist.*
import alexx.rizz.mytodo.feature.todolist.TodoListVM.*
import alexx.rizz.mytodo.feature.todolist.ui.*
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
import androidx.compose.ui.graphics.*
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
  val containerColor = if (item.isDone) MyColors.DoneCard else Color.Transparent
  Card(
    modifier = Modifier
      .makeDraggable(this, interactionSource, haptic, onDragStopped),
    interactionSource = interactionSource,
    shape = RoundedCornerShape(7.dp),
    colors = CardDefaults.cardColors(containerColor = containerColor),
    onClick = {},
  ) {
    Box(Modifier.conditional(!item.isDone, { drawBackground(MyColors.UndoneGradientColors) })) {
      Row(
        Modifier.padding(RowPadding),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        DoneCheckBox(item, onDoneClick = onDoneClick)
        Spacer(Modifier.width(10.dp))
        ItemText(Modifier.weight(1f), item.text, item.isDone)
        EditButton(item.isDone, onEditClick)
      }
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
private fun ItemText(modifier: Modifier, text: String, isDone: Boolean) {
  Text(
    text,
    modifier = modifier,
    fontSize = 18.sp,
    lineHeight = 18.sp,
    color = getItemTextColor(isDone)
  )
}

@Composable
private fun EditButton(isDone: Boolean, onClick: () -> Unit) {
  CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
    IconButton(onClick) {
      val editIcon = Icons.Default.Edit
      Icon(editIcon, null, tint = editIcon.tintColor.getItemButtonIconColor(isDone))
    }
  }
}