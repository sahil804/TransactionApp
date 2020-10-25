package com.example.cbasample.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.cbasample.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

const val ONE_MINUTE: Long = 1000 * 60
const val ONE_HOUR: Long = ONE_MINUTE * 60
const val ONE_DAY: Long = ONE_HOUR * 24
const val ONE_MONTH: Long = ONE_DAY * 30
const val ONE_YEAR: Long = ONE_DAY * 365

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.setVisibility(isVisible: Boolean) {
    if (isVisible) show() else hide()
}

fun Context.hasPermission(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(this, permission) ==
        PackageManager.PERMISSION_GRANTED
}

fun Fragment.requestPermissionWithRationale(
    permission: String,
    requestCode: Int
) {
    val provideRationale = shouldShowRequestPermissionRationale(permission)

    if (!provideRationale) {
        requestPermissions(arrayOf(permission), requestCode)
    }
}

fun Date.displayDate(format: String = "dd MMM yyyy"): String =
    SimpleDateFormat(format, Locale.ENGLISH).format(this)

fun Date.differenceDays(nextDate: Date): Long {
    Log.d("Sahil", "Date diff between " + nextDate + " old date " + this)
    val diffDays = nextDate.time - time
    return TimeUnit.DAYS.convert(diffDays, TimeUnit.MILLISECONDS)
}

fun String.parseToDate(format: String = "dd/MM/yyyy") = try {
    SimpleDateFormat(format, Locale.ROOT).parse(this)
} catch (e: ParseException) {
    Date()
}

fun String.getSpannedText(): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT);
    } else {
        return Html.fromHtml(this);
    }
}

fun Date.getRelativeDateFromToday(context: Context): String {
    val diffDate = Date().time - time
    return when {
        diffDate < ONE_MINUTE -> {
            context.getString(R.string.just_now)
        }
        diffDate in ONE_MINUTE until ONE_HOUR -> {
            val minutes = diffDate / ONE_MINUTE
            context.resources.getQuantityString(
                R.plurals.minutes_text,
                minutes.toInt(),
                minutes
            )
        }
        diffDate in ONE_HOUR until ONE_DAY -> {
            val hours = diffDate / ONE_HOUR
            context.resources.getQuantityString(R.plurals.hours_text, hours.toInt(), hours)
        }
        diffDate in ONE_DAY until ONE_MONTH -> {
            val days = diffDate / ONE_DAY
            context.resources.getQuantityString(R.plurals.days_text, days.toInt(), days)
        }
        diffDate in ONE_MONTH until ONE_YEAR -> {
            val months = diffDate / ONE_MONTH
            context.resources.getQuantityString(R.plurals.months_text, months.toInt(), months)
        }
        else -> {
            val years = diffDate / ONE_YEAR
            context.resources.getQuantityString(R.plurals.years_text, years.toInt(), years)
        }
    }
}