package br.com.home.ui.dvdview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.com.home.R
import br.com.home.databinding.DvdViewActivityBinding
import br.com.home.ui.components.DvdMessage
import org.koin.android.ext.android.inject

class DvdViewActivity : AppCompatActivity() {

    private val viewModel: DvdViewModel by inject()

    private val binding by lazy { DvdViewActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        bindObservables()
        bindListeners()
        viewModel.readAllMessagesLocal()
    }

    private fun bindObservables() {
        viewModel.messageResult.observe(this) {
            binding.buttonMessage.hideProgress()
            it.success?.let {
                Toast.makeText(
                    this@DvdViewActivity,
                    getString(R.string.request_sucess),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.saveMessage(it)
                binding.dvdScreensaverView.addMessage(DvdMessage(it.id, it.title))
            }

            it?.error?.let {
                Toast.makeText(this@DvdViewActivity, getString(it), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.dvdMessageList.observe(this) {
            it.forEach {
                binding.dvdScreensaverView.addMessage(it)
            }
        }
    }

    private fun bindListeners() {
        binding.buttonMessage.setOnButtonClickListener {
            viewModel.getMessage(binding.dvdScreensaverView.getMessages().last().id)
        }

        binding.toggleButton.setOnCheckedChangeListener { _, _ ->
            viewModel.toggleRealRequest()
        }
    }
}