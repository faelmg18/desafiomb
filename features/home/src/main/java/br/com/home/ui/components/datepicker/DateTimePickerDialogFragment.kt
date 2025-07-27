package br.com.home.ui.components.datepicker

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import br.com.home.R
import br.com.home.ui.components.datepicker.model.DateTimePickerConfig
import br.com.home.ui.components.datepicker.model.DateTimePickerResult
import java.util.Calendar

class MBPopOverDateTimePickerDialogFragment(
    private val config: DateTimePickerConfig,
    private val onResult: (DateTimePickerResult) -> Unit
) : DialogFragment() {

    init {
        require(config.showDatePicker || config.showTimePicker) {
            getString(R.string.error_picker)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireContext()
        val calendar = Calendar.getInstance()

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        val scrollView = ScrollView(context).apply {
            addView(layout)
        }

        val datePicker = DatePicker(context).apply {
            visibility = if (config.showDatePicker) View.VISIBLE else View.GONE

            config.defaultDate?.let {
                calendar.time = it
                updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }

            config.minDate?.let {
                val minCalendar = Calendar.getInstance().apply {
                    time = it
                }
                minDate = minCalendar.timeInMillis
            }

            config.maxDate?.let {
                val maxCalendar = Calendar.getInstance().apply {
                    time = it
                }
                maxDate = maxCalendar.timeInMillis
            }

            config.backgroundColor?.let { setBackgroundColor(ContextCompat.getColor(context, it)) }
        }

        customizeDatePickerHeaderById(
            datePicker,
            config.headerColor?.let { ContextCompat.getColor(context, it) },
            config.headerTextColor?.let { ContextCompat.getColor(context, it) }
        )

        setDatePickerTextColor(datePicker, ContextCompat.getColor(context, R.color.white))

        val timePicker = TimePicker(context).apply {
            setIs24HourView(true)
            visibility = if (config.showTimePicker) View.VISIBLE else View.GONE
            config.defaultTime?.let { (hour, minute) ->
                this.hour = hour
                this.minute = minute
            }
            config.backgroundColor?.let { setBackgroundColor(it) }
        }

        if (config.showDatePicker) layout.addView(datePicker)
        if (config.showTimePicker) layout.addView(timePicker)

        return AlertDialog.Builder(context)
            .setTitle(config.dialogTitle ?: R.string.select_date_or_hour)
            .setView(scrollView)
            .setPositiveButton(config.positiveButtonText ?: R.string.ok) { _, _ ->
                val pickedDate = if (config.showDatePicker) {
                    Calendar.getInstance().apply {
                        set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.time
                } else null

                val pickedTime = if (config.showTimePicker) {
                    timePicker.hour to timePicker.minute
                } else null

                onResult(DateTimePickerResult(pickedDate, pickedTime))
            }
            .setNegativeButton(config.cancelButtonText ?: R.string.cancel) { _, _ -> dismiss() }
            .create()
    }

    // De fato não consegui mudar a cor do header sem ser por algum style, ou seja de forma programatica,
    // tive que fazer uma pesquisa na internet e o que consegui achar foi isso.
    // Desculpa pessoal, nessa aqui eu realmente não consegui mesmo e tive que recorrer a internet pra isso.
    fun customizeDatePickerHeaderById(
        datePicker: DatePicker,
        headerBackgroundColor: Int? = null,
        headerTextColor: Int? = null
    ) {
        try {
            val headerId =
                Resources.getSystem().getIdentifier("date_picker_header", "id", "android")
            val headerView = datePicker.findViewById<ViewGroup>(headerId)

            headerView?.let {
                headerBackgroundColor?.let { it1 -> it.setBackgroundColor(it1) }
                for (i in 0 until it.childCount) {
                    val child = it.getChildAt(i)
                    if (child is TextView) {
                        headerTextColor?.let { it1 -> child.setTextColor(it1) }
                    } else if (child is ViewGroup) {
                        for (j in 0 until child.childCount) {
                            val subChild = child.getChildAt(j)
                            if (subChild is TextView) {
                                headerTextColor?.let { it1 -> subChild.setTextColor(it1) }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun applyTextColorToAllTextViews(viewGroup: ViewGroup, color: Int) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)

            when (child) {
                is TextView -> child.setTextColor(color)
                is ViewGroup -> applyTextColorToAllTextViews(child, color)
            }
        }
    }

    fun setDatePickerTextColor(datePicker: DatePicker, color: Int) {

        val dayPickerViewGroup = datePicker.findViewById<ViewGroup>(
            Resources.getSystem().getIdentifier("date_picker_day_picker", "id", "android")
        )


        applyTextColorToAllTextViews(dayPickerViewGroup, Color.WHITE)
    }

}
