package alexx.rizz.mytodo.feature.todolist

import androidx.room.*

@Entity(tableName = "todoLists")
data class TodoListEntity(
  val text: String,
  val sortId: Int,
  @PrimaryKey(autoGenerate = true) val id: TodoListId = TodoListId.Unknown,
)