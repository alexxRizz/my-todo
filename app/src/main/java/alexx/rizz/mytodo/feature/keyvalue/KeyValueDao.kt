package alexx.rizz.mytodo.feature.keyvalue

import androidx.room.*
import kotlinx.coroutines.flow.*

@Dao
interface KeyValueDao {

  @Upsert
  suspend fun insert(e: KeyValueEntity)

  @Query("SELECT value FROM keyValues WHERE akey=:key")
  suspend fun value(key: String): String?

  @Query("SELECT value FROM keyValues WHERE akey=:key")
  fun observeValue(key: String): Flow<String?>

  @Query("SELECT COUNT(1) FROM keyValues")
  suspend fun count(): Int
}