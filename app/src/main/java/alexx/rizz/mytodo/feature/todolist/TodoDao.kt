package alexx.rizz.mytodo.feature.todolist

import androidx.room.*
import kotlinx.coroutines.flow.*

@Dao
interface TodoDao {

  @Upsert
  suspend fun insert(e: TodoEntity)

  @Query("SELECT COUNT(1) FROM todos")
  suspend fun count(): Int

  @Query("SELECT * FROM todos ORDER BY orderNumber")
  fun all(): Flow<List<TodoEntity>>
}