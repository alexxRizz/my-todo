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

  @Query("SELECT todoLists.*, COALESCE(SUM(todoItems.isDone), 0) doneCount, COUNT(todoItems.id) itemCount FROM todoLists " +
    "LEFT JOIN todoItems ON todoLists.id = todoItems.listOwnerId " +
    "GROUP BY todoLists.id " +
    "ORDER BY sortId")
  fun observeAll(): Flow<List<TodoListDto>>

  @Query("SELECT todoLists.*, COALESCE(SUM(todoItems.isDone), 0) doneCount, COUNT(todoItems.id) itemCount FROM todoLists " +
    "LEFT JOIN todoItems ON todoLists.id = todoItems.listOwnerId " +
    "WHERE todoLists.id=:id " +
    "GROUP BY todoLists.id")
  suspend fun byId(id: TodoListId): TodoListDto?

  @Query("SELECT MAX(sortId) FROM todoLists")
  suspend fun getMaxSortId(): Int

  @Query("UPDATE todoLists SET sortId=:sortId WHERE id=:id")
  suspend fun updateListOrder(id: TodoListId, sortId: Int)
}

data class TodoListDto(
  @Embedded val entity: TodoListEntity,
  val doneCount: Int,
  val itemCount: Int,
)