package br.com.desafiomercantil.navigation.login

import android.content.Context
import android.content.Intent
import br.com.home.ui.HomeActivity
import br.com.login.navigation.LoginNavigation

class LoginNavigationImpl : LoginNavigation {
    override fun navigateToHome(context: Context) {
        context.startActivity(Intent(context, HomeActivity::class.java))
    }
}