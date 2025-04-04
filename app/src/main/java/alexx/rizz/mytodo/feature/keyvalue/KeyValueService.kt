package alexx.rizz.mytodo.feature.keyvalue

import alexx.rizz.mytodo.feature.todolist.*
import kotlinx.coroutines.flow.*
import javax.inject.*

interface IKeyValueService {

  fun observeCurrentListOwnerId(): Flow<TodoListId>
  suspend fun setCurrentListOwnerId(id: TodoListId)
}

@Singleton
class KeyValueService @Inject constructor(
  private val mKeyValueRep: IKeyValueRepository
) : IKeyValueService {

  override fun observeCurrentListOwnerId(): Flow<TodoListId> =
    observeInt(Keys.CurrentListOwnerId)
      .map { if (it == null) TodoListId.Unknown else TodoListId(it) }

  override suspend fun setCurrentListOwnerId(id: TodoListId) {
    setInt(Keys.CurrentListOwnerId, id.asInt)
  }

  private fun observeInt(key: String): Flow<Int?> =
    mKeyValueRep.observeValue(key).map { it?.toIntOrNull() }

  // private suspend fun getInt(key: String): Int? =
  //   mKeyValueRep.getValue(key)?.toIntOrNull()

  private suspend fun setInt(key: String, value: Int) {
    mKeyValueRep.setValue(key, value.toString())
  }
}

private object Keys {
  const val CurrentListOwnerId = "CurrentListOwnerId"
}