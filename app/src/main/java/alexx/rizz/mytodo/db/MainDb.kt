package alexx.rizz.mytodo.db

import alexx.rizz.mytodo.feature.todolist.*
import androidx.room.*

@Database(
  entities = [
    TodoEntity::class,
  ],
  exportSchema = true,
  version = MainDbVersion,
)
abstract class MainDb : RoomDatabase() {

  abstract fun todo(): TodoDao
}