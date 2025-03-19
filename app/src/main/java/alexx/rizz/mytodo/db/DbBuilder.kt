package ru.ilexx.kassir.posserver.db

import alexx.rizz.mytodo.app.logging.*

private val Log = getLogger<DbBuilder>()

object DbBuilder {

  // fun buildMain(inMemory: Boolean, dbPath: String): MainDb {
  //   val builder = if (inMemory)
  //     Room.inMemoryDatabaseBuilder<MainDb>()
  //   else
  //     Room.databaseBuilder<MainDb>(dbPath)
  //   return builder
  //     .setDriver(BundledSQLiteDriver())
  //     .addCallback(MainDbCallback(dbPath))
  //     // .addMigrations() TODO: migrations
  //     .setQueryCoroutineContext(Dispatchers.IO)
  //     .build()
  // }
  //
  // private class MainDbCallback(
  //   private val mDbPath: String,
  // ) : RoomDatabase.Callback() {
  //
  //   override fun onCreate(connection: SQLiteConnection) {
  //     Log.d("$mDbPath created: currentVersion=${connection.getCurrentDbVersion()}")
  //   }
  //
  //   override fun onOpen(connection: SQLiteConnection) {
  //     Log.d("$mDbPath opened: currentVersion=${connection.getCurrentDbVersion()} expectedVersion=$MainDbVersion")
  //   }
  //
  //   private fun SQLiteConnection.getCurrentDbVersion(): Int =
  //     this.prepare("PRAGMA user_version").use {
  //       it.step()
  //       it.getInt(0)
  //     }
  // }
}