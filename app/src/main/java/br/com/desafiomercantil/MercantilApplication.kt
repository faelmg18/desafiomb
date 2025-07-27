package br.com.desafiomercantil

import android.app.Application
import br.com.auth.di.authModules
import br.com.desafiomercantil.core.di.coreModules
import br.com.desafiomercantil.di.appModules
import br.com.home.di.homeModules
import br.com.login.di.loginModules
import br.com.shared.di.sharedModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MercantilApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Como aqui é um projeto pequeno, não tem problema carregar todas as dependências de
            // uma vez, mas o ideial é que cada module carregue as dependencias quando realmente precisar
            // para evitar sobrecarregar a inicialização do app
            androidContext(this@MercantilApplication)
            modules(appModules + coreModules + loginModules + authModules + homeModules + sharedModules)
        }
    }
}