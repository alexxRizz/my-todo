package alexx.rizz.mytodo.feature.todolist

import androidx.room.*

@Entity(tableName = "todoItems")
data class TodoItemEntity(
  val text: String,
  val isDone: Boolean,
  val orderNumber: Int,
  val listOwnerId: TodoListId,
  @PrimaryKey(autoGenerate = true) val id: TodoItemId = TodoItemId.Unknown,
)