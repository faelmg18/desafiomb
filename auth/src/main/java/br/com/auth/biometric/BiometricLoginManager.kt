package br.com.auth.biometric

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import br.com.auth.R
import br.com.desafiomercantil.core.cacheprovider.CacheProvider
import br.com.desafiomercantil.core.uuid.MercantilUUID

class BiometricLoginManager(
    private val context: Context,
    private val uuidGenerator: MercantilUUID
) : LoginManager {

    override fun authenticate(
        activity: FragmentActivity,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
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
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.login_with_biometrics))
            .setSubtitle(context.getString(R.string.use_your_fingerprint_or_face_to_enter))
            .setNegativeButtonText(context.getString(R.string.cancel))
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}