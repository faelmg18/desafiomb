package br.com.home.ui.user

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.com.home.R
import br.com.home.databinding.UserActivityBinding
import br.com.shared.domain.model.LoggedInUser
import org.koin.android.ext.android.inject

class UserActivity : AppCompatActivity() {

    private val binding by lazy { UserActivityBinding.inflate(layoutInflater) }
    private val viewModel: UserViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        bindObservables()
        viewModel.getUser()
    }

    private fun bindObservables() {
        viewModel.userResult.observe(this) {
            onGetUser(it)
        }
    }

    private fun onGetUser(user: LoggedInUser) = with(binding) {
        userId.text = getString(R.string.user_id, user.userUUID)
    }
}