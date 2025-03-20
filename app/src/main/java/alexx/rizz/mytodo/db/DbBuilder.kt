package alexx.rizz.mytodo.db

import alexx.rizz.mytodo.app.logging.*
import android.content.*
import androidx.room.*
import androidx.sqlite.db.*

private val Log = getLogger<DbBuilder>()

object DbBuilder {

  fun buildMain(context: Context, inMemory: Boolean, dbName: String = "main.db"): MainDb {
    val builder = if (inMemory)
      Room.inMemoryDatabaseBuilder(context, MainDb::class.java)
    else
      Room.databaseBuilder(context, MainDb::class.java, dbName)
    return builder
      // .addMigrations() TODO: migrations
      .addCallback(MainDbCallback(dbName))
      .build()
  }

  private class MainDbCallback(
    private val mDbName: String,
  ) : RoomDatabase.Callback() {

    override fun onCreate(connection: SupportSQLiteDatabase) {
      Log.d("$mDbName created: currentVersion=${connection.version}")
    }

    override fun onOpen(connection: SupportSQLiteDatabase) {
      Log.d("$mDbName opened: currentVersion=${connection.version} expectedVersion=$MainDbVersion")
    }
  }
}