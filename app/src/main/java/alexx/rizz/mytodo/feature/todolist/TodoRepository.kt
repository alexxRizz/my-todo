package alexx.rizz.mytodo.feature.todolist

import alexx.rizz.mytodo.db.*
import kotlinx.coroutines.flow.*
import javax.inject.*

interface ITodoRepository {
  fun getAll(): Flow<List<TodoItem>>
}

@Singleton
class TodoRepository @Inject constructor(
  dbProvider: IMainDbProvider
) : RepositoryBase(dbProvider), ITodoRepository {

  private val mDao get() = db.todo()

  override fun getAll(): Flow<List<TodoItem>> =
    mDao.all().map { entities -> entities.map { it.toDomain() } }
}

private fun TodoEntity.toDomain(): TodoItem =
  TodoItem(
    id = this.id,
    orderNumber = this.orderNumber,
    text = this.text,
    isDone = this.isDone,
  )