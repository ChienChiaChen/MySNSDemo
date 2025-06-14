package com.chiachen.mysnsdemo.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Long.toFormattedTime(pattern: String = "yyyy/MM/dd HH:mm"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(this))
}