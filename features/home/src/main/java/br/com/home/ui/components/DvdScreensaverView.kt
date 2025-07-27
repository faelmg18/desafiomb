package br.com.home.ui.components

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.doOnLayout
import br.com.home.databinding.ViewDvdScreensaverBinding
import kotlin.random.Random

class DvdScreensaverView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding =
        ViewDvdScreensaverBinding.inflate(LayoutInflater.from(context), this, true)

    private var dx = 8
    private var dy = 8
    private val handler = Handler(Looper.getMainLooper())
    private val messages = mutableListOf(DvdMessage(0, "Mercantil do Brasil"))
    private var currentMessageIndex = 0

    init {
        nextMessage()

        doOnLayout {
            startMoving()
            startMessageSwitcher()
            binding.floatingMessage.setOnClickListener {
                handler.removeCallbacks(runnable)
                nextMessage()
            }
        }
    }

    private val runnable = Runnable { nextMessage() }

    private fun startMoving() = with(binding) {
        handler.post(object : Runnable {
            override fun run() {
                val parentWidth = width
                val parentHeight = height
                val viewWidth = floatingMessage.width
                val viewHeight = floatingMessage.height

                val x = floatingMessage.x + dx
                val y = floatingMessage.y + dy

                if (x <= 0 || x + viewWidth >= parentWidth) {
                    dx *= -1
                    changeColor()
                }

                if (y <= 0 || y + viewHeight >= parentHeight) {
                    dy *= -1
                    changeColor()
                }

                floatingMessage.x = x
                floatingMessage.y = y
                handler.postDelayed(this, 16)
            }
        })
    }

    private fun changeColor() {
        val rnd = Random
        val color = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        binding.floatingMessage.setTextColor(color)
    }

    private fun startMessageSwitcher() {
        handler.postDelayed(runnable, 5000)
    }

    private fun nextMessage() {
        binding.apply {
            currentMessageIndex = (currentMessageIndex + 1) % messages.size
            floatingMessage.text = messages[currentMessageIndex].message

            // É necessário aguardar o layout ser calculado, pois ao trocar o texto,
            // o tamanho da TextView pode mudar. Se isso acontecer quando ela estiver
            // próxima de uma das bordas, a nova largura pode ultrapassar os limites do container (view parent),
            // fazendo com que a TextView fique travada no canto onde estava anteriormente.
            // Esse método abaixo é pra corrigir isso.

            floatingMessage.doOnLayout {
                val parentWidth = width
                val parentHeight = height
                val viewWidth = floatingMessage.width
                val viewHeight = floatingMessage.height

                var newX = floatingMessage.x
                var newY = floatingMessage.y

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

                floatingMessage.x = newX
                floatingMessage.y = newY
            }

            handler.postDelayed(runnable, 5000)
        }
    }

    fun addMessage(message: DvdMessage) {
        messages.add(message)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacksAndMessages(null)
    }

    fun getMessages() = messages

    fun setContainerColor(backgroundColor: Int) {
        binding.floatingMessage.setBackgroundColor(backgroundColor)
    }
}

data class DvdMessage(val id: Int, val message: String)
