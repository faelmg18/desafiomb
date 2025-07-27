package br.com.home.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import br.com.home.R

class ButtonWithProgressView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val button: Button
    private val progressBar: ProgressBar
    private var onClickListener: () -> Unit = {}

    init {
        LayoutInflater.from(context).inflate(R.layout.button_with_progress, this, true)

        button = findViewById(R.id.button)
        progressBar = findViewById(R.id.progressBar)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonWithProgressView)

        val buttonText = typedArray.getString(R.styleable.ButtonWithProgressView_buttonText)
        buttonText?.let {
            button.text = it
        }

        typedArray.recycle()
        button.setOnClickListener {
            showProgress()
            onClickListener()
        }
    }

    fun showProgress() {
        progressBar.visibility = VISIBLE
        button.isEnabled = false
    }

    fun hideProgress() {
        progressBar.visibility = GONE
        button.isEnabled = true
    }

    fun setButtonText(text: String) {
        button.text = text
    }

    fun setOnButtonClickListener(onClickListener: () -> Unit) {
        this.onClickListener = onClickListener
    }
}
