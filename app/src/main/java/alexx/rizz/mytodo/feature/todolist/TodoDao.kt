package alexx.rizz.mytodo.feature.todolist

import androidx.room.*
import kotlinx.coroutines.flow.*

@Dao
interface TodoDao {

  @Upsert
  suspend fun upsert(e: TodoEntity)

  @Query("UPDATE todos SET isDone=:isDone WHERE id=:todoId")
  suspend fun done(todoId: Int, isDone: Boolean)

  @Query("UPDATE todos SET text=:text WHERE id=:todoId")
  suspend fun updateTodo(todoId: Int, text: String)

  @Query("SELECT COUNT(1) FROM todos")
  suspend fun count(): Int

  @Query("SELECT * FROM todos ORDER BY orderNumber")
  fun all(): Flow<List<TodoEntity>>

  @Query("SELECT * FROM todos WHERE id=:todoId")
  suspend fun byId(todoId: Int): TodoEntity?

  @Query("SELECT MAX(orderNumber) FROM todos")
  suspend fun getMaxOrderNumber(): Int
}