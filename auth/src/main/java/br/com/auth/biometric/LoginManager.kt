package br.com.auth.biometric

import androidx.fragment.app.FragmentActivity

interface LoginManager {
    fun authenticate(
        activity: FragmentActivity,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    )
}