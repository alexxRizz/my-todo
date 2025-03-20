package alexx.rizz.mytodo.db

import androidx.room.*

interface IRepository {

  suspend fun <R> inTransaction(block: suspend () -> R): R
}

abstract class RepositoryBase(private val mDbProvider: IMainDbProvider) : IRepository {

  protected val db get() = mDbProvider.db

  override suspend fun <R> inTransaction(block: suspend () -> R): R =
    db.withTransaction(block)
}