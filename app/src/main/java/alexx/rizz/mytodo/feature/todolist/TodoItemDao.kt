package alexx.rizz.mytodo.feature.todolist

import androidx.room.*
import kotlinx.coroutines.flow.*

@Dao
interface TodoItemDao {

  @Upsert
  suspend fun upsert(e: TodoItemEntity)

  @Query("UPDATE todoItems SET isDone=:isDone WHERE id=:id")
  suspend fun done(id: TodoItemId, isDone: Boolean)

  @Query("UPDATE todoItems SET text=:text WHERE id=:id")
  suspend fun updateTodo(id: TodoItemId, text: String)

  @Query("SELECT COUNT(1) FROM todoItems")
  suspend fun count(): Int

  @Query("SELECT * FROM todoItems WHERE listOwnerId=:listOwnerId ORDER BY orderNumber")
  fun byListOwnerId(listOwnerId: TodoListId): Flow<List<TodoItemEntity>>

  @Query("SELECT * FROM todoItems WHERE id=:id")
  suspend fun byId(id: TodoItemId): TodoItemEntity?

  @Query("SELECT MAX(orderNumber) FROM todoItems")
  suspend fun getMaxOrderNumber(): Int
}