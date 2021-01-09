package com.zirouan.unphoto.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    companion object {

        const val DATE_FORMAT_API = "yyyy-MM-dd"
        const val DATE_FORMAT_LOCAL = "MM/dd/yyyy"
        const val DATE_TIME_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss"

        private fun getDate(dateAsString: String?, pattern: String): Date? {
            dateAsString?.let {
                val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                try {
                    return simpleDateFormat.parse(dateAsString)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }

            return null
        }

        fun formatDate(dateAsString: String?, oldFormat: String, newFormat: String): String {
            dateAsString?.let {
                val date = SimpleDateFormat(oldFormat, Locale.US).let {
                    it.timeZone = TimeZone.getTimeZone("GMT+00:00")
                    it.parse(dateAsString)
                }

                date?.let {
                    return SimpleDateFormat(newFormat, Locale.getDefault()).format(date)
                }
//                val date = SimpleDateFormat(oldFormat, Locale.getDefault()).parse(dateAsString)
//                date?.let {
//                    return SimpleDateFormat(newFormat, Locale.getDefault()).format(date)
//                }
            }

            return ""
        }

        fun isLessThanCurrent(dateAsString: String?): Boolean {
            val date = getDate(dateAsString, DATE_FORMAT_LOCAL)
            date?.let {
                return it.before(Calendar.getInstance().time)
            }

            return true
        }

        fun isGreaterThan(dateAsString: String?, dateReference: String): Boolean {
            val date = getDate(dateAsString, DATE_FORMAT_LOCAL)
            val dateRef = getDate(dateReference, DATE_FORMAT_LOCAL)
            date?.let {
                return it.after(dateRef)
            }

            return true
        }

        fun getMonth(date: String?): Int {
            date?.let {
                try {
                    return date.substring(3, 5).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            return 0
        }

        fun getDay(date: String?): Int {
            date?.let {
                try {
                    return date.substring(0, 2).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            return 0
        }

        fun increaseTime(
                dateAsString: String?,
                format: String,
                calendarTimeType: Int,
                amountToIncrease: Int
        ): String? {
            if (dateAsString.isNullOrEmpty()) {
                return null
            }

            val date = getDate(dateAsString, format) ?: return null
            val newDate = increaseTime(date, calendarTimeType, amountToIncrease)
            return SimpleDateFormat(format, Locale.getDefault()).format(newDate)
        }

        fun increaseTime(date: Date, calendarTimeType: Int, amountToIncrease: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(calendarTimeType, amountToIncrease)

            return calendar.time
        }
    }
}