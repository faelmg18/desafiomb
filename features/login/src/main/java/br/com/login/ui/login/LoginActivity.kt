package br.com.login.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.com.auth.biometric.LoginManager
import br.com.login.R
import br.com.login.databinding.ActivityLoginBinding
import br.com.login.navigation.LoginNavigation
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by inject()
    private val loginManager: LoginManager by inject()
    private val loginNavigation: LoginNavigation by inject()
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        initListeners()
        bindObservables()
        loginViewModel.isUserLogged()
    }

    private fun initListeners() = with(binding) {
        buttonAuth.setOnClickListener {
            loginManager.authenticate(this@LoginActivity, { userUUID ->
                onUserAuthenticated(userUUID)
            }) {
                onErrorOnUserAuthenticateUser(it)
            }
        }
    }

    private fun bindObservables() {
        loginViewModel.isUserLogged.observe(this) { isUserLogged ->
            if (isUserLogged) {
                navigateToHome()
            }
        }
    }

    private fun onErrorOnUserAuthenticateUser(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
            .show()
    }

    private fun onUserAuthenticated(userUUID: String) {
        Toast.makeText(
            applicationContext,
            getString(R.string.user_auth_success),
            Toast.LENGTH_SHORT
        ).show()
        loginViewModel.onAuthSuccess(userUUID)
        navigateToHome()
    }

    private fun navigateToHome() {
        loginNavigation.navigateToHome(this)
        finish()
    }
}