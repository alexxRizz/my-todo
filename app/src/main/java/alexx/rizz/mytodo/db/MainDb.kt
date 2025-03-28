package alexx.rizz.mytodo.db

import alexx.rizz.mytodo.feature.todolist.*
import androidx.room.*

@Database(
  entities = [
    TodoItemEntity::class,
    TodoListEntity::class,
  ],
  exportSchema = true,
  version = MainDbVersion,
)
abstract class MainDb : RoomDatabase() {

  abstract fun todoItem(): TodoItemDao
  abstract fun todoList(): TodoListDao
}