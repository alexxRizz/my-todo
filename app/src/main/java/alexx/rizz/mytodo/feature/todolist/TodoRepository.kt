package alexx.rizz.mytodo.feature.todolist

import alexx.rizz.mytodo.db.*
import androidx.room.*
import kotlinx.coroutines.flow.*
import javax.inject.*

interface ITodoRepository {
  fun getAll(): Flow<List<TodoItem>>
  suspend fun addTodo(todo: TodoItem)
}

@Singleton
class TodoRepository @Inject constructor(
  dbProvider: IMainDbProvider
) : RepositoryBase(dbProvider), ITodoRepository {

  private val mDao get() = db.todo()

  override fun getAll(): Flow<List<TodoItem>> =
    mDao.all().map { entities -> entities.map { it.toDomain() } }

  override suspend fun addTodo(todo: TodoItem) {
    db.withTransaction {
      val maxOrderNumber = mDao.getMaxOrderNumber()
      mDao.insert(todo.toEntity(maxOrderNumber + 1))
    }
  }
}

private fun TodoEntity.toDomain(): TodoItem =
  TodoItem(
    id = this.id,
    text = this.text,
    isDone = this.isDone,
  )

private fun TodoItem.toEntity(orderNumber: Int): TodoEntity =
  TodoEntity(
    text = this.text,
    isDone = this.isDone,
    orderNumber = orderNumber,
    id = this.id
  )