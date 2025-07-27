package br.com.home.ui.datepicker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.com.home.R
import br.com.home.databinding.MbDatePickerActivityBinding
import br.com.home.ui.components.datepicker.MBPopOverDateTimePickerDialogFragment
import br.com.home.ui.components.datepicker.model.DateTimePickerConfig
import br.com.home.ui.components.datepicker.model.DateTimePickerResult
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
class MBDatePickerActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_TAG = "dateTimePicker"
        private const val FORMAT_TIME = "%02d:%02d"
        private const val FORMAT_DATE = "dd/MM/yyyy"
    }

    private val binding by lazy { MbDatePickerActivityBinding.inflate(layoutInflater) }
    private val format by lazy { SimpleDateFormat(FORMAT_DATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        bindListeners()
    }

    private fun bindListeners() = with(binding) {
        date.setOnClickListener {
            showDatePicker()
        }

        time.setOnClickListener {
            showTimePicker()
        }
        dateTime.setOnClickListener {
            showDateTimePicker()
        }
    }

    private fun showDatePicker() {
        val defaultDate = Calendar.getInstance().apply {
            set(2025, Calendar.JULY, 24)
        }.time

        // Pego a data de hoje mais 10 dias pra frente
        // so pra testar se ira bloquear as datas da forma certa
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 10)
        val futureDate = calendar.time

        val config = DateTimePickerConfig(
            showDatePicker = true,
            showTimePicker = false,
            defaultDate = defaultDate,
            defaultTime = 14 to 30, // um exemplo de colocar hora aqui no caso 14:30
            minDate = Date(),
            maxDate = futureDate,
            backgroundColor = R.color.blue,
            headerColor = R.color.pink,
            headerTextColor = R.color.orange,
            dialogTitle = R.string.select_date
        )

        val picker = MBPopOverDateTimePickerDialogFragment(config) { result: DateTimePickerResult ->
            val date = result.date?.let { format.format(it) }
            binding.date.text = "$date"
        }

        picker.show(supportFragmentManager, FRAGMENT_TAG)
    }

    private fun showTimePicker() {
        val defaultDate = Calendar.getInstance().apply {
            set(2025, Calendar.JULY, 24)
        }.time

        val config = DateTimePickerConfig(
            showDatePicker = false,
            showTimePicker = true,
            defaultDate = defaultDate,
            minDate = null,
            maxDate = null,
            backgroundColor = R.color.progress_background,
            dialogTitle = R.string.select_hour
        )

        val picker = MBPopOverDateTimePickerDialogFragment(config) { result: DateTimePickerResult ->
            val formattedTime =
                String.format(Locale.ROOT, FORMAT_TIME, result.time?.first, result.time?.second)
            binding.time.text = formattedTime
        }

        picker.show(supportFragmentManager, FRAGMENT_TAG)
    }

    private fun showDateTimePicker() {
        val defaultDate = Calendar.getInstance().apply {
            set(2025, Calendar.JULY, 24)
        }.time

        // Pego a data de hoje mais 10 dias pra frente
        // so pra testar se ira bloquear as datas da forma certa
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 10)
        val futureDate = calendar.time

        val config = DateTimePickerConfig(
            showDatePicker = true,
            showTimePicker = true,
            defaultDate = defaultDate,
            defaultTime = 14 to 30, // um exemplo de colocar hora aqui no caso 14:30
            minDate = Date(),
            maxDate = futureDate,
            backgroundColor = R.color.progress_background
        )

        val picker = MBPopOverDateTimePickerDialogFragment(config) { result: DateTimePickerResult ->
            val date = result.date?.let { format.format(it) }
            val formattedTime =
                String.format(Locale.ROOT, FORMAT_TIME, result.time?.first, result.time?.second)

            binding.dateTime.text = getString(R.string.date_hour_formatted, date, formattedTime)
        }

        picker.show(supportFragmentManager, FRAGMENT_TAG)
    }
}