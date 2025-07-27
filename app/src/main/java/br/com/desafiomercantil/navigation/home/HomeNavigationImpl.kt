package br.com.desafiomercantil.navigation.home

import android.content.Context
import android.content.Intent
import br.com.home.navigation.HomeNavigation
import br.com.login.ui.login.LoginActivity

class HomeNavigationImpl : HomeNavigation {
    override fun navigateToLogin(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }
}