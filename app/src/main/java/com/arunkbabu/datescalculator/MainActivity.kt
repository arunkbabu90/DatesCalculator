package com.arunkbabu.datescalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.util.Pair
import com.arunkbabu.datescalculator.databinding.ActivityMainBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.lang.ArithmeticException
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.time.temporal.UnsupportedTemporalTypeException
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val dialogTag = "DatePickerTag"
    private val TAG = MainActivity::class.java.simpleName
    private var view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        // Date Fab Click
        binding.fabPickDate.setOnClickListener {
            // Launch Date Picker
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_date))
//                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(supportFragmentManager, dialogTag)

            // On Date Selected
            datePicker.addOnPositiveButtonClickListener { millis ->
                val todayMills = MaterialDatePicker.todayInUtcMilliseconds()
                val tc: TemporalCapsule = getAllFromDate(startDateMills = millis, endDateMills = todayMills)
                printResults(tc)
            }
        }

        // Date Range Fab Click
        binding.fabPickDates.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(getString(R.string.select_dates))
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

            datePicker.show(supportFragmentManager, dialogTag)

            // On Date Range Selected
            datePicker.addOnPositiveButtonClickListener { range ->
                val startMillis = range?.first ?: -1
                val endMillis = range?.second ?: -1
                val tc: TemporalCapsule = getAllFromDate(startDateMills = startMillis, endDateMills = endMillis)
                printResults(tc)
            }
        }
    }

    private fun getAllFromDate(startDateMills: Long, endDateMills: Long): TemporalCapsule {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MM yyyy")
        val cal = Calendar.getInstance(TimeZone.getDefault())

        cal.timeInMillis = startDateMills
        val startMonth = String.format("%02d", cal.get(Calendar.MONTH) + 1)
        val startDay = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH))
        val startYear = cal.get(Calendar.YEAR)
        val startDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val startWeek = cal.get(Calendar.WEEK_OF_MONTH)
        val startDateString = "$startDay $startMonth $startYear"

        cal.timeInMillis = endDateMills
        val endMonth = String.format("%02d", cal.get(Calendar.MONTH) + 1)
        val endDay = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH))
        val endYear = cal.get(Calendar.YEAR)
        val endDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val endWeek = cal.get(Calendar.WEEK_OF_MONTH)
        val endDateString = "$endDay $endMonth $endYear"

        var daysPassed: Long = 0
        var yearsPassed: Long = 0
        var monthsPassed: Long = 0
        var weeksPassed: Long = 0
        var hoursPassed: Long = 0
        var minutesPassed: Long = 0
        var secondsPassed: Long = 0

        Log.d(TAG, "$startDay $startMonth $startYear to $endDay $endMonth $endYear")
        try {
            val startDate = LocalDate.parse(startDateString, formatter)
            val endDate = LocalDate.parse(endDateString, formatter)
            daysPassed = ChronoUnit.DAYS.between(startDate, endDate)
            yearsPassed = ChronoUnit.YEARS.between(startDate, endDate)
            monthsPassed = ChronoUnit.MONTHS.between(startDate, endDate)
            weeksPassed = ChronoUnit.WEEKS.between(startDate, endDate)
//            hoursPassed = ChronoUnit.HOURS.between(startDate, endDate)
//            minutesPassed = ChronoUnit.MINUTES.between(startDate, endDate)
//            secondsPassed = ChronoUnit.SECONDS.between(startDate, endDate)
        } catch (e: DateTimeParseException) {
            Toast.makeText(this, getString(R.string.err_date_parse), Toast.LENGTH_SHORT).show()
        } catch (e: DateTimeException) {
            Toast.makeText(this, getString(R.string.err_date_calculation), Toast.LENGTH_SHORT).show()
        } catch (e: UnsupportedTemporalTypeException) {
            Toast.makeText(this, getString(R.string.err_unsupported_temporal_type), Toast.LENGTH_SHORT).show()
        } catch (e: ArithmeticException) {
            Toast.makeText(this, getString(R.string.err_number_overflow), Toast.LENGTH_SHORT).show()
        }

        return TemporalCapsule(
            day1 = startDay.toInt(),
            day2 = endDay.toInt(),
            dayOfWeek1 = startDayOfWeek,
            dayOfWeek2 = endDayOfWeek,
            weekOfMonth1 = startWeek,
            weekOfMonth2 = endWeek,
            month1 = startMonth.toInt(),
            month2 = endMonth.toInt(),
            year1 = startYear,
            year2 = endYear,
            daysPassed = daysPassed,
            yearsPassed = yearsPassed,
            monthsPassed = monthsPassed,
            weeksPassed = weeksPassed,
            hoursPassed = hoursPassed,
            minutesPassed = minutesPassed,
            secondsPassed = secondsPassed
        )
    }

    private fun printResults(tc: TemporalCapsule) {
        val monthStr1 = tc.month1.toMonthString()
        val monthStr2 = tc.month2.toMonthString()
        val weekStr1 = tc.dayOfWeek1.toWeekString()
        val weekStr2 = tc.dayOfWeek2.toWeekString()
        val correctDaysPassed = tc.daysPassed.toCorrectDaysPassed()

        val printString = "${tc.day1} $monthStr1 ${tc.year1}, $weekStr1\nTo\n${tc.day2} $monthStr2 ${tc.year2}, $weekStr2\n\n\n" +
                "Days Passed: ${correctDaysPassed}\n" +
                "Weeks Passed: ${tc.weeksPassed.toString().trimStart('-')}\n" +
                "Months Passed: ${tc.monthsPassed.toString().trimStart('-')}\n" +
                "Years Passed: ${tc.yearsPassed.toString().trimStart('-')}\n"
//                        "Hours Passed: ${tc.hoursPassed}\n" +
//                        "Minutes Passed: ${tc.minutesPassed}\n" +
//                        "Seconds Passed: ${tc.secondsPassed}"

        binding.textView.text = printString
    }
}