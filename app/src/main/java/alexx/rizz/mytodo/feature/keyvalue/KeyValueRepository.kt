package alexx.rizz.mytodo.feature.keyvalue

import alexx.rizz.mytodo.db.*
import kotlinx.coroutines.flow.*
import javax.inject.*

interface IKeyValueRepository : IRepository {

  fun observeValue(key: String): Flow<String?>
  suspend fun getValue(key: String): String?
  suspend fun setValue(key: String, value: String)
}

@Singleton
class KeyValueRepository @Inject constructor(
  dbProvider: IMainDbProvider
) : RepositoryBase(dbProvider), IKeyValueRepository {

  private val mDao get() = db.keyValue()

  override fun observeValue(key: String): Flow<String?> =
    mDao.observeValue(key)

  override suspend fun getValue(key: String): String? =
    mDao.value(key)

  override suspend fun setValue(key: String, value: String) =
    mDao.insert(KeyValueEntity(key, value))
}