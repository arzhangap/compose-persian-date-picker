package com.github.arzhangap.compose_persian_date.jalaicalender

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale


/**
 * Model for JalaliCalendar
 */
class JalaliCalendar {
    /**
     * @return year
     */
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0

    /**
     * Today's Jalali date
     */
    constructor() {
        fromGregorian(GregorianCalendar())
    }

    /**
     * Create a JalaliCalendar object
     * @param year Jalali Year
     * @param month Jalali Month
     * @param day Jalali Day
     */
    constructor(year: Int, month: Int, day: Int) {
        set(year, month, day)
    }


    /**
     * Create a JalaliCalendar object from gregorian calendar
     * @param gc gregorian calendar object
     */
    constructor(gc: GregorianCalendar) {
        fromGregorian(gc)
    }


    /**
     * Create a JalaliCalendar object from Localdate(java 8)
     */
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(localDate: LocalDate) {
        fromGregorian(GregorianCalendar.from(localDate.atStartOfDay(ZoneId.systemDefault())))
    }

    /**
     * Create a JalaliCalendar object from Date object
     * @param date Date object
     */
    constructor(date: Date) {
        val gc = GregorianCalendar()
        gc.time = date
        fromGregorian(gc)
    }

    /**
     * Convert current jalali date to gregorian date
     * @return date converted gregorianDate
     */
    private fun toGregorian(): GregorianCalendar {
        val julianDay = toJulianDay()
        return julianDayToGregorianCalendar(julianDay)
    }

    /**
     * set date from gregorian date
     * @param gc input gregorian calendar
     */
    private fun fromGregorian(gc: GregorianCalendar) {
        val jd = gregorianToJulianDayNumber(gc)
        fromJulianDay(jd)
    }

    val yesterday: JalaliCalendar
        /**
         * @return yesterday date
         */
        get() = getDateByDiff(-1)

    val tomorrow: JalaliCalendar
        /**
         * @return tomorrow date
         */
        get() = getDateByDiff(1)

    /**
     * get Jalali date by day difference
     * @param diff number of day diffrents
     * @return jalali calendar diffحزن
     */
    private fun getDateByDiff(diff: Int): JalaliCalendar {
        val gc = toGregorian()
        gc.add(Calendar.DAY_OF_MONTH, diff)
        return JalaliCalendar(gc)
    }

    val dayOfWeek: Int
        /**
         * @return day Of Week
         */
        get() = toGregorian().get(Calendar.DAY_OF_WEEK)

    val firstDayOfWeek: Int
        /**
         * @return get first day of week
         */
        get() = toGregorian().firstDayOfWeek

    private val dayOfWeekString: String
        /**
         *
         * @return day name
         */
        get() = when (dayOfWeek) {
            1 -> "یک‌شنبه"
            2 -> "دوشنبه"
            3 -> "سه‌شنبه"
            4 -> "چهارشنبه"
            5 -> "پنجشنبه"
            6 -> "جمعه"
            7 -> "شنبه"
            else -> "نامعلوم"
        }

    val monthString: String
        /**
         *
         * @return month name
         */
        get() {
            return when (month) {
                1 -> "فروردین"
                2 -> "اردیبهشت"
                3 -> "خرداد"
                4 -> "تیر"
                5 -> "مرداد"
                6 -> "شهریور"
                7 -> "مهر"
                8 -> "آبان"
                9 -> "آذر"
                10 -> "دی"
                11 -> "بهمن"
                12 -> "اسفند"
                else -> "نامعلوم"
            }
        }


    private val dayOfWeekDayMonthString: String = "$dayOfWeekString $day $monthString"
        /**
         * get String with the following format :
         * یکشنبه ۱۲ آبان
         * @return String format
         */
//        get() =

    val isLeap: Boolean
        /**
         *
         * @return return whether this year is a jalali leap year
         */
        get() = getLeapFactor(year) == 0

    val yearLength: Int
        /**
         * @return the length of the current year. 366 for leap years and 365 for normal
         */
        get() = if (isLeap) 366 else 365

    val monthLength: Int
        /**
         * @return return length of the jalalic month
         */
        get() {
            if (month < 7) {
                return 31
            } else if (month < 12) {
                return 30
            } else if (month == 12) {
                return if (isLeap) 30
                else 29
            }
            return 0
        }

    /**
     * @param year year number to set
     * @param month month number to set
     * @param day day number to set
     */
    fun set(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as JalaliCalendar

        return year == that.year && month == that.month && day == that.day
    }

    private fun gregorianToJulianDayNumber(gc: GregorianCalendar): Int {
        val gregorianYear = gc[GregorianCalendar.YEAR]
        val gregorianMonth = gc[GregorianCalendar.MONTH] + 1
        val gregorianDay = gc[GregorianCalendar.DAY_OF_MONTH]

        return (((1461 * (gregorianYear + 4800 + (gregorianMonth - 14) / 12)) / 4
                + (367 * (gregorianMonth - 2 - 12 * ((gregorianMonth - 14) / 12))) / 12
                - (3 * ((gregorianYear + 4900 + (gregorianMonth - 14) / 12) / 100)) / 4 + gregorianDay
                - 32075) - (gregorianYear + 100100 + (gregorianMonth - 8) / 6) / 100 * 3 / 4 + 752)
    }

    private fun julianToJulianDayNumber(jc: JulianCalendar): Int {
        val julianYear = jc.year
        val julianMonth = jc.month
        val julianDay = jc.day

        return ((1461 * (julianYear + 4800 + (julianMonth - 14) / 12)) / 4
                + (367 * (julianMonth - 2 - 12 * ((julianMonth - 14) / 12))) / 12
                - (3 * ((julianYear + 4900 + (julianMonth - 14) / 12) / 100)) / 4 + julianDay
                - 32075)
    }

    private fun julianDayToGregorianCalendar(julianDayNumber: Int): GregorianCalendar {
        val j =
            4 * julianDayNumber + 139361631 + (4 * julianDayNumber + 183187720) / 146097 * 3 / 4 * 4 - 3908
        val i = (j % 1461) / 4 * 5 + 308

        val gregorianDay = (i % 153) / 5 + 1
        val gregorianMonth = ((i / 153) % 12) + 1
        val gregorianYear = j / 1461 - 100100 + (8 - gregorianMonth) / 6

        return GregorianCalendar(gregorianYear, gregorianMonth - 1, gregorianDay)
    }

    private fun fromJulianDay(julianDayNumber: Int) {
        val gc = julianDayToGregorianCalendar(julianDayNumber)
        val gregorianYear = gc[GregorianCalendar.YEAR]
        val jalaliMonth: Int
        val jalaliDay: Int

        var jalaliYear: Int = gregorianYear - 621

        val gregorianFirstFarvardin =
            JalaliCalendar(jalaliYear, 1, 1).gregorianFirstFarvardin
        val julianDayFarvardinFirst = gregorianToJulianDayNumber(gregorianFirstFarvardin)
        var diffFromFarvardinFirst = julianDayNumber - julianDayFarvardinFirst



        if (diffFromFarvardinFirst >= 0) {
            if (diffFromFarvardinFirst <= 185) {
                jalaliMonth = 1 + diffFromFarvardinFirst / 31
                jalaliDay = (diffFromFarvardinFirst % 31) + 1
                set(jalaliYear, jalaliMonth, jalaliDay)
                return
            } else {
                diffFromFarvardinFirst -= 186
            }
        } else {
            diffFromFarvardinFirst += 179
            if (getLeapFactor(jalaliYear) == 1) diffFromFarvardinFirst += 1
            jalaliYear -= 1
        }


        jalaliMonth = 7 + diffFromFarvardinFirst / 30
        jalaliDay = (diffFromFarvardinFirst % 30) + 1
        set(jalaliYear, jalaliMonth, jalaliDay)
    }

    private fun toJulianDay(): Int {
        val jalaliMonth = month
        val jalaliDay = day

        val gregorianFirstFarvardin = gregorianFirstFarvardin
        val gregorianYear = gregorianFirstFarvardin[Calendar.YEAR]
        val gregorianMonth = gregorianFirstFarvardin[Calendar.MONTH] + 1
        val gregorianDay = gregorianFirstFarvardin[Calendar.DAY_OF_MONTH]

        val julianFirstFarvardin = JulianCalendar(gregorianYear, gregorianMonth, gregorianDay)


        val julianDay =
            (julianToJulianDayNumber(julianFirstFarvardin) + (jalaliMonth - 1) * 31 - jalaliMonth / 7 * (jalaliMonth - 7)
                    + jalaliDay - 1)

        return julianDay
    }


    private val gregorianFirstFarvardin: GregorianCalendar
        get() {
            var marchDay = 0
            val breaks = intArrayOf(
                -61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
                1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178
            )

            val jalaliYear = year
            val gregorianYear = jalaliYear + 621
            var jalaliLeap = -14
            var jp = breaks[0]

            var jump: Int
            for (j in 1..19) {
                val jm = breaks[j]
                jump = jm - jp
                if (jalaliYear < jm) {
                    val n = jalaliYear - jp
                    jalaliLeap += n / 33 * 8 + (n % 33 + 3) / 4

                    if ((jump % 33) == 4 && (jump - n) == 4) jalaliLeap += 1

                    val gregorianLeap =
                        (gregorianYear / 4) - ((gregorianYear / 100 + 1) * 3 / 4) - 150

                    marchDay = 20 + (jalaliLeap - gregorianLeap)

                    if ((jump - n) < 6) {
                        n - jump + (jump + 4) / 33 * 33
                    }

                    break
                }

                jalaliLeap += jump / 33 * 8 + (jump % 33) / 4
                jp = jm
            }

            return GregorianCalendar(gregorianYear, 2, marchDay)
        }

    private fun getLeapFactor(jalaliYear: Int): Int {
        var leap = 0
        val breaks = intArrayOf(
            -61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
            1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178
        )

        var jp = breaks[0]

        var jump: Int
        for (j in 1..19) {
            val jm = breaks[j]
            jump = jm - jp
            if (jalaliYear < jm) {
                var n = jalaliYear - jp

                if ((jump - n) < 6) n = n - jump + (jump + 4) / 33 * 33

                leap = ((((n + 1) % 33) - 1) % 4)

                if (leap == -1) leap = 4

                break
            }

            jp = jm
        }

        return leap
    }

    override fun toString(): String {
        return String.format(Locale.getDefault(),"%04d-%02d-%02d", year, month, day)
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + month
        result = 31 * result + day
        result = 31 * result + dayOfWeekDayMonthString.hashCode()
        return result
    }


    private class JulianCalendar(var year: Int, var month: Int, var day: Int)
}