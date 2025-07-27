package br.com.home.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.com.home.databinding.HomeBinding
import br.com.home.navigation.HomeNavigation
import br.com.home.ui.datepicker.MBDatePickerActivity
import br.com.home.ui.dvdview.DvdViewActivity
import br.com.home.ui.state.HomeActions
import br.com.home.ui.user.UserActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val binding by lazy { HomeBinding.inflate(layoutInflater) }
    private val homeViewModel: HomeViewModel by inject()
    private val homeNavigation: HomeNavigation by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        bindListeners()
        bindObservables()
    }

    private fun bindObservables() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.actions.collect { action ->
                    when (action) {
                        is HomeActions.NavigateToLogin -> homeNavigation.navigateToLogin(
                            context = this@HomeActivity
                        ).apply {
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun bindListeners() = with(binding) {
        buttonGoToPopOver.setOnClickListener {
            gotoActivity(MBDatePickerActivity::class.java)
        }

        buttonGoToDvdView.setOnClickListener {
            gotoActivity(DvdViewActivity::class.java)
        }

        buttonGoToUserData.setOnClickListener {
            gotoActivity(UserActivity::class.java)
        }

        buttonLogout.setOnClickListener {
            homeViewModel.logout()
        }
    }

    private fun <T> gotoActivity(clazz: Class<T>) {
        Intent(this@HomeActivity, clazz).run(::startActivity)
    }
}