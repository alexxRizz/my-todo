package alexx.rizz.mytodo.db

import android.content.*
import androidx.room.*
import dagger.hilt.android.qualifiers.*
import javax.inject.*

interface IMainDbProvider {
  val db: MainDb
}

@Singleton
class MainDbInitializer @Inject constructor(
  @ApplicationContext private val mContext: Context,
) : IMainDbProvider {

  override lateinit var db: MainDb

  fun init() {
    db = DbBuilder.buildMain(mContext, inMemory = false)
    db.open()
  }
}

private fun RoomDatabase.open() {
  this.openHelper.readableDatabase
}