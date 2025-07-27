package br.com.home.ui.components.datepicker.model

import java.util.Date

data class DateTimePickerConfig(
    val showDatePicker: Boolean = true,
    val showTimePicker: Boolean = true,
    val defaultDate: Date? = null,
    val defaultTime: Pair<Int, Int>? = null,
    val minDate: Date? = null,
    val maxDate: Date? = null,
    val backgroundColor: Int? = null,
    val headerColor: Int? = null,
    val headerTextColor: Int? = null,
    val dialogTitle: Int? = null,
    val cancelButtonText: Int? = null,
    val positiveButtonText: Int? = null
)
