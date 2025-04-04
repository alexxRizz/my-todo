package alexx.rizz.mytodo.feature.todolist

import alexx.rizz.mytodo.app.logging.*
import alexx.rizz.mytodo.db.*
import androidx.room.*
import kotlinx.coroutines.flow.*
import javax.inject.*

interface ITodoRepository : IRepository {
  fun observeItems(listOwnerId: TodoListId): Flow<List<TodoItem>>
  suspend fun getItemById(id: TodoItemId): TodoItem?
  suspend fun addItem(item: TodoItem)
  suspend fun doneItem(id: TodoItemId, isDone: Boolean)
  suspend fun updateItem(id: TodoItemId, text: String)
  suspend fun removeItem(id: TodoItemId)
  suspend fun reorderItems(reordered: List<TodoItem>)

  fun observeLists(): Flow<List<TodoList>>
  suspend fun getListById(id: TodoListId): TodoList?
  suspend fun addList(list: TodoList)
  suspend fun updateList(id: TodoListId, text: String)
  suspend fun removeList(id: TodoListId)
  suspend fun reorderLists(reordered: List<TodoList>)
}

private val Log = getLogger<TodoRepository>()

@Singleton
class TodoRepository @Inject constructor(
  dbProvider: IMainDbProvider
) : RepositoryBase(dbProvider), ITodoRepository {

  private val mItemDao get() = db.todoItem()
  private val mListDao get() = db.todoList()

  override fun observeItems(listOwnerId: TodoListId): Flow<List<TodoItem>> =
    mItemDao
      .observeByListOwnerId(listOwnerId)
      .map { entities -> entities.map { it.toDomain() } }

  override suspend fun getItemById(id: TodoItemId): TodoItem? =
    mItemDao.byId(id)?.toDomain()

  override suspend fun addItem(item: TodoItem) {
    db.withTransaction {
      val maxOrderNumber = mItemDao.getMaxOrderNumber()
      mItemDao.upsert(item.toEntity(maxOrderNumber + 1))
    }
    Log.i("Added $item")
  }

  override suspend fun doneItem(id: TodoItemId, isDone: Boolean) {
    mItemDao.done(id, isDone)
  }

  override suspend fun updateItem(id: TodoItemId, text: String) {
    mItemDao.update(id, text)
  }

  override suspend fun removeItem(id: TodoItemId) {
    mItemDao.delete(id)
  }

  override suspend fun reorderItems(reordered: List<TodoItem>) {
    inTransaction {
      reordered.forEachIndexed { i, it ->
        mItemDao.updateItemOrder(it.id, i)
      }
    }
  }

  override fun observeLists(): Flow<List<TodoList>> =
    mListDao
      .observeAll()
      .map { entities -> entities.map { it.toDomain() } }

  override suspend fun getListById(id: TodoListId): TodoList? =
    mListDao.byId(id)?.toDomain()

  override suspend fun addList(list: TodoList) {
    inTransaction {
      val maxOrderNumber = mListDao.getMaxOrderNumber()
      mListDao.upsert(list.toEntity(maxOrderNumber + 1))
    }
    Log.i("Added $list")
  }

  override suspend fun updateList(id: TodoListId, text: String) {
    mListDao.updateList(id, text)
  }

  override suspend fun removeList(id: TodoListId) {
    mListDao.delete(id)
  }

  override suspend fun reorderLists(reordered: List<TodoList>) {
    inTransaction {
      reordered.forEachIndexed { i, it ->
        mListDao.updateListOrder(it.id, i)
      }
    }
  }
}

private fun TodoItemEntity.toDomain(): TodoItem =
  TodoItem(
    id = this.id,
    text = this.text,
    isDone = this.isDone,
    listOwnerId = this.listOwnerId
  )

private fun TodoItem.toEntity(orderNumber: Int): TodoItemEntity =
  TodoItemEntity(
    text = this.text,
    isDone = this.isDone,
    orderNumber = orderNumber,
    listOwnerId = this.listOwnerId,
    id = this.id,
  )

private fun TodoListEntity.toDomain(): TodoList =
  TodoList(
    id = this.id,
    text = this.text,
  )

private fun TodoList.toEntity(orderNumber: Int): TodoListEntity =
  TodoListEntity(
    text = this.text,
    orderNumber = orderNumber,
    id = this.id,
  )