package br.com.home.ui.components

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.doOnLayout
import br.com.home.R
import kotlin.random.Random

class DvdScreensaverView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val messageView: TextView
    private var dx = 8
    private var dy = 8
    private val handler = Handler(Looper.getMainLooper())
    private val messages = mutableListOf(DvdMessage(0, "Android 💚"))
    private var currentMessageIndex = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.view_dvd_screensaver, this, true)
        messageView = findViewById(R.id.floatingMessage)
        nextMessage()

        doOnLayout {
            startMoving()
            startMessageSwitcher()
            messageView.setOnClickListener {
                handler.removeCallbacks(runnable)
                nextMessage()
            }
        }
    }

    private val runnable = Runnable { nextMessage() }

    private fun startMoving() {
        handler.post(object : Runnable {
            override fun run() {
                val parentWidth = width
                val parentHeight = height
                val viewWidth = messageView.width
                val viewHeight = messageView.height

                val x = messageView.x + dx
                val y = messageView.y + dy

                if (x <= 0 || x + viewWidth >= parentWidth) {
                    dx *= -1
                    changeColor()
                }

                if (y <= 0 || y + viewHeight >= parentHeight) {
                    dy *= -1
                    changeColor()
                }

                messageView.x = x
                messageView.y = y
                handler.postDelayed(this, 16)
            }
        })
    }

    private fun changeColor() {
        val rnd = Random
        val color = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        messageView.setTextColor(color)
    }

    private fun startMessageSwitcher() {
        handler.postDelayed(runnable, 5000)
    }

    private fun nextMessage() {
        currentMessageIndex = (currentMessageIndex + 1) % messages.size
        messageView.text = messages[currentMessageIndex].message

        // É necessário aguardar o layout ser calculado, pois ao trocar o texto,
        // o tamanho da TextView pode mudar. Se isso acontecer quando ela estiver
        // próxima de uma das bordas, a nova largura pode ultrapassar os limites do container (view parent),
        // fazendo com que a TextView fique travada no canto onde estava anteriormente.
        // Esse método abaixo é pra corrigir isso.

        messageView.doOnLayout {
            val parentWidth = width
            val parentHeight = height
            val viewWidth = messageView.width
            val viewHeight = messageView.height

            var newX = messageView.x
            var newY = messageView.y

            if (newX + viewWidth > parentWidth) {
                newX = (parentWidth - viewWidth).coerceAtLeast(0).toFloat()
                dx *= -1
            } else if (newX < 0) {
                newX = 0f
                dx *= -1
            }

            if (newY + viewHeight > parentHeight) {
                newY = (parentHeight - viewHeight).coerceAtLeast(0).toFloat()
                dy *= -1
            } else if (newY < 0) {
                newY = 0f
                dy *= -1
            }

            messageView.x = newX
            messageView.y = newY
        }

        handler.postDelayed(runnable, 5000)
    }

    fun addMessage(message: DvdMessage) {
        messages.add(message)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacksAndMessages(null)
    }

    fun getMessages() = messages
}

data class DvdMessage(val id: Int, val message: String)
