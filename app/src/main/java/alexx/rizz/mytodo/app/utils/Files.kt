package alexx.rizz.mytodo.app.utils

import java.io.*

val File.requireParent: File
  get() = this.parentFile ?: throw IllegalStateException("Parent dir not exists in path $this")

fun File.file(name: String): File =
  File(this, name)

fun File.file(name: String, vararg names: String): File =
  File(this, (listOf(name) + names).joinToString(File.separator))

fun File.recreateDir() {
  deleteRecursively()
  mkdirs()
}

fun File.recreateParentDir() =
  requireParent.recreateDir()
