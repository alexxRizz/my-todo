package alexx.rizz.mytodo.feature.keyvalue

import alexx.rizz.mytodo.feature.todolist.*
import javax.inject.*

interface IKeyValueService {

  suspend fun getCurrentListOwnerId(): TodoListId
  suspend fun setCurrentListOwnerId(id: TodoListId)
}

@Singleton
class KeyValueService @Inject constructor(
  private val mKeyValueRep: IKeyValueRepository
) : IKeyValueService {

  override suspend fun getCurrentListOwnerId(): TodoListId {
    val asInt = getInt(Keys.CurrentListOwnerId)
    return if (asInt == null) TodoListId.Unknown else TodoListId(asInt)
  }

  override suspend fun setCurrentListOwnerId(id: TodoListId) {
    setInt(Keys.CurrentListOwnerId, id.asInt)
  }

  private suspend fun getInt(key: String): Int? =
    mKeyValueRep.getValue(key)?.toIntOrNull()

  private suspend fun setInt(key: String, value: Int) {
    mKeyValueRep.setValue(key, value.toString())
  }
}

private object Keys {
  const val CurrentListOwnerId = "CurrentListOwnerId"
}