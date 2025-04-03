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
import androidx.compose.ui.unit.*
import sh.calvin.reorderable.*

@Composable
fun TodoLists(
  modifier: Modifier,
  lists: List<TodoList>,
  onClick: (TodoListId) -> Unit,
  onEditClick: (TodoListId) -> Unit,
  onDragStopped: (UserIntent.ReorderLists) -> Unit
) {
  val haptic = rememberReorderHapticFeedback()
  var reordered by rememberUpdatedStateMutable(lists)
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
    items(reordered, key = { it.id.asInt }) { list ->
      ReorderableItem(reorderableLazyListState, list.id.asInt) {
        ListRow(list, haptic,
          onClick = onClick,
          onEditClick = onEditClick,
          onDragStopped = { onDragStopped(UserIntent.ReorderLists(reordered)) }
        )
      }
    }
  }
}

@Composable
private fun ReorderableCollectionItemScope.ListRow(
  list: TodoList,
  haptic: ReorderHapticFeedback,
  onClick: (TodoListId) -> Unit,
  onEditClick: (TodoListId) -> Unit,
  onDragStopped: () -> Unit,
) {
  val interactionSource = remember { MutableInteractionSource() }
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .makeDraggable(this, interactionSource, haptic, onDragStopped),
    interactionSource = interactionSource,
    shape = RoundedCornerShape(5.dp),
    colors = CardDefaults.cardColors(containerColor = MyColors.UndoneCard),
    onClick = { onClick(list.id) },
  ) {
    Row(
      Modifier.padding(RowPadding),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        modifier = Modifier.weight(1f),
        text = list.text,
        fontSize = 18.sp,
      )
      CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        IconButton(onClick = { onEditClick(list.id) }) { Icon(Icons.Default.Edit, null) }
      }
    }
  }
}