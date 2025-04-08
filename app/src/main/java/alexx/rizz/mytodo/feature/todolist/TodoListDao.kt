package alexx.rizz.mytodo.feature.todolist

import androidx.room.*
import kotlinx.coroutines.flow.*

@Dao
interface TodoListDao {

  @Upsert
  suspend fun upsert(e: TodoListEntity)

  @Query("UPDATE todoLists SET text=:text WHERE id=:id")
  suspend fun updateList(id: TodoListId, text: String)

  @Query("DELETE FROM todoLists WHERE id=:id")
  suspend fun delete(id: TodoListId)

  @Query("SELECT COUNT(1) FROM todoLists")
  suspend fun count(): Int

  @Query("SELECT * FROM todoLists ORDER BY sordId")
  fun observeAll(): Flow<List<TodoListEntity>>

  @Query("SELECT * FROM todoLists WHERE id=:id")
  suspend fun byId(id: TodoListId): TodoListEntity?

  @Query("SELECT MAX(sordId) FROM todoLists")
  suspend fun getMaxSortId(): Int

  @Query("UPDATE todoLists SET sordId=:sordId WHERE id=:id")
  suspend fun updateListOrder(id: TodoListId, sordId: Int)
}