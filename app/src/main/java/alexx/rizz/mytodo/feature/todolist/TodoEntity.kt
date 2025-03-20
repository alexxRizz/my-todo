package alexx.rizz.mytodo.feature.todolist

import androidx.room.*

@Entity(tableName = "todos")
data class TodoEntity(
  val text: String,
  val isDone: Boolean,
  val orderNumber: Int,
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
)