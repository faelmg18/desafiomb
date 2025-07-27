package br.com.home.ui.components.datepicker.model

import java.util.Date

data class DateTimePickerResult(
    val date: Date?,
    val time: Pair<Int, Int>?
)
