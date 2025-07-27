package br.com.home.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.shared.domain.model.LoggedInUser
import br.com.shared.domain.repository.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userResult = MutableLiveData<LoggedInUser>()
    val userResult: LiveData<LoggedInUser> = _userResult

    fun getUser() {
        val user = userRepository.getUser()
        user?.let { _userResult.value = it }
    }
}