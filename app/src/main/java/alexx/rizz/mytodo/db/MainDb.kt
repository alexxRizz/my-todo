package alexx.rizz.mytodo.db

import alexx.rizz.mytodo.feature.keyvalue.*
import alexx.rizz.mytodo.feature.todolist.*
import androidx.room.*

@Database(
  entities = [
    KeyValueEntity::class,
    TodoItemEntity::class,
    TodoListEntity::class,
  ],
  exportSchema = true,
  version = MainDbVersion,
)
abstract class MainDb : RoomDatabase() {

  abstract fun keyValue(): KeyValueDao
  abstract fun todoItem(): TodoItemDao
  abstract fun todoList(): TodoListDao
}