package br.com.auth.biometric

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import br.com.auth.R
import br.com.desafiomercantil.core.uuid.MercantilUUID
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class BiometricLoginManagerTest {

    private val context: Context = mockk(relaxed = true)
    private val uuidGenerator: MercantilUUID = mockk(relaxed = true)
    private lateinit var biometricLoginManager: LoginManager

    @Before
    fun setUp() {
        biometricLoginManager = BiometricLoginManager(context, uuidGenerator)
        every { ContextCompat.getMainExecutor(context) } returns Runnable::run
    }

    @Test
    fun `onAuthenticationSucceeded should call onSuccess with uuid`() {
        val uuid = "fake-uuid-123"
        every { uuidGenerator.getUUID() } returns uuid

        var successResult: String? = null
        var errorResult: String? = null

        val callback = createAuthenticationCallback(
            onSuccess = { successResult = it },
            onError = { errorResult = it }
        )

        callback.onAuthenticationSucceeded(mockk(relaxed = true))

        assertEquals(uuid, successResult)
        assertEquals(null, errorResult)
    }

    @Test
    fun `onAuthenticationError should call onError with formatted string`() {
        val errorMessage = "Sensor bloqueado"
        val formattedMessage = "Erro: $errorMessage"
        every { context.getString(R.string.error_on_autheticate, errorMessage) } returns formattedMessage

        var successResult: String? = null
        var errorResult: String? = null

        val callback = createAuthenticationCallback(
            onSuccess = { successResult = it },
            onError = { errorResult = it }
        )

        callback.onAuthenticationError(1, errorMessage)

        assertEquals(null, successResult)
        assertEquals(formattedMessage, errorResult)
    }

    @Test
    fun `onAuthenticationFailed should call onError with authentication failed message`() {
        val failMessage = "Autenticação falhou"
        every { context.getString(R.string.authentication_failed) } returns failMessage

        var successResult: String? = null
        var errorResult: String? = null

        val callback = createAuthenticationCallback(
            onSuccess = { successResult = it },
            onError = { errorResult = it }
        )

        callback.onAuthenticationFailed()

        assertEquals(null, successResult)
        assertEquals(failMessage, errorResult)
    }

    private fun createAuthenticationCallback(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ): BiometricPrompt.AuthenticationCallback {
        return object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                val uuid = uuidGenerator.getUUID()
                onSuccess(uuid)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onError(context.getString(R.string.error_on_autheticate, errString))
            }

            override fun onAuthenticationFailed() {
                onError(context.getString(R.string.authentication_failed))
            }
        }
    }
}
