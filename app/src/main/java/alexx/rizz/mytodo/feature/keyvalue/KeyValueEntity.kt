package alexx.rizz.mytodo.feature.keyvalue

import androidx.room.*

@Entity(tableName = "keyValues")
data class KeyValueEntity(
  @PrimaryKey @ColumnInfo(name = "akey") val key: String,
  val value: String,
)