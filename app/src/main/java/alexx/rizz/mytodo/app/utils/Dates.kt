@file:Suppress("unused")

package alexx.rizz.mytodo.app.utils

import java.text.*
import java.util.*

private const val Iso8601Format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

object Dates {
  val Min = fromString("1970.01.01 00:00:00", "yyyy.MM.dd HH:mm:ss")

  fun now() = Date()

  @Suppress("MemberVisibilityCanBePrivate")
  fun fromString(s: String, format: String): Date =
    SimpleDateFormat(format, Locale.ROOT).parse(s)
      ?: throw RuntimeException("Ошибка парсинга даты: $s format=$format")

  fun fromStringIso8601(s: String): Date =
    fromString(s, Iso8601Format)
}

fun Date.toString(format: String): String =
  SimpleDateFormat(format, Locale.ROOT).format(this)

fun Date.toStringIso8601(): String =
  toString(Iso8601Format)

fun Date.removeTime(): Date {
  val calendar = Calendar.getInstance()
  calendar.time = this
  calendar[Calendar.HOUR_OF_DAY] = 0
  calendar[Calendar.MINUTE] = 0
  calendar[Calendar.SECOND] = 0
  calendar[Calendar.MILLISECOND] = 0
  return calendar.time
}

fun Date.removeMinutes(): Date {
  val calendar = Calendar.getInstance()
  calendar.time = this
  calendar[Calendar.MINUTE] = 0
  calendar[Calendar.SECOND] = 0
  calendar[Calendar.MILLISECOND] = 0
  return calendar.time
}

fun Date.removeSeconds(): Date {
  val calendar = Calendar.getInstance()
  calendar.time = this
  calendar[Calendar.SECOND] = 0
  calendar[Calendar.MILLISECOND] = 0
  return calendar.time
}

fun Date.addYear(year: Int): Date =
  addValue(Calendar.YEAR, year)

fun Date.addDay(day: Int): Date =
  addValue(Calendar.DAY_OF_MONTH, day)

fun Date.addHour(hour: Int): Date =
  addValue(Calendar.HOUR_OF_DAY, hour)

fun Date.addMinute(minute: Int): Date =
  addValue(Calendar.MINUTE, minute)

fun Date.addSecond(second: Int): Date =
  addValue(Calendar.SECOND, second)

private fun Date.addValue(what: Int, value: Int): Date {
  if (value == 0)
    return this
  val calendar = Calendar.getInstance()
  calendar.time = this
  calendar.add(what, value)
  return calendar.time
}

fun Date.setHour(hour: Int): Date =
  setValue(Calendar.HOUR_OF_DAY, hour)

fun Date.setTime(hour: Int, minute: Int, second: Int, millisecond: Int): Date {
  val calendar = Calendar.getInstance()
  calendar.time = this
  calendar[Calendar.HOUR_OF_DAY] = hour
  calendar[Calendar.MINUTE] = minute
  calendar[Calendar.SECOND] = second
  calendar[Calendar.MILLISECOND] = millisecond
  return calendar.time
}

fun Date.setTimeToEndOfDay(): Date =
  setTime(23, 59, 59, 999)

private fun Date.setValue(what: Int, value: Int): Date {
  val calendar = Calendar.getInstance()
  calendar.time = this
  calendar.set(what, value)
  return calendar.time
}

fun Date.getHour(): Int =
  getValue(Calendar.HOUR_OF_DAY)

fun Date.getSecond(): Int =
  getValue(Calendar.SECOND)

fun Date.getMillisecond(): Int =
  getValue(Calendar.MILLISECOND)

private fun Date.getValue(what: Int): Int {
  val calendar = Calendar.getInstance()
  calendar.time = this
  return calendar[what]
}
