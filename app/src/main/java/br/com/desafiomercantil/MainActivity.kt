package br.com.desafiomercantil

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.com.desafiomercantil.core.cacheprovider.CacheProvider
import br.com.home.data.datasource.local.MessageLocalDataSourceImpl.Companion.MESSAGE_KEY
import br.com.home.ui.HomeActivity
import br.com.login.ui.login.LoginActivity
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val cacheProvider: CacheProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        redirectToLogin()
    }

    private fun redirectToLogin() {
        Intent(this, LoginActivity::class.java).run(::startActivity)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        cacheProvider.clearByKey(MESSAGE_KEY)
    }
}