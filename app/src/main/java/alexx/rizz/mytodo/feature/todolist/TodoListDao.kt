package alexx.rizz.mytodo.feature.todolist

import androidx.room.*
import kotlinx.coroutines.flow.*

@Dao
interface TodoListDao {

  @Upsert
  suspend fun upsert(e: TodoListEntity)

  @Query("UPDATE todoLists SET text=:text WHERE id=:id")
  suspend fun updateList(id: TodoListId, text: String)

  @Query("SELECT COUNT(1) FROM todoLists")
  suspend fun count(): Int

  @Query("SELECT * FROM todoLists ORDER BY orderNumber")
  fun observeAll(): Flow<List<TodoListEntity>>

  @Query("SELECT * FROM todoLists WHERE id=:id")
  suspend fun byId(id: Int): TodoListEntity?

  @Query("SELECT MAX(orderNumber) FROM todoLists")
  suspend fun getMaxOrderNumber(): Int
}