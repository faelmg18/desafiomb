package br.com.login.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.login.domain.repository.LoginRepository
import br.com.login.mapper.toLoggedInUser

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _isUserLogged = MutableLiveData<Boolean>()
    val isUserLogged: LiveData<Boolean> = _isUserLogged

    fun isUserLogged() {
        _isUserLogged.value = loginRepository.isLoggedIn()
    }

    fun onAuthSuccess(userUUID: String) {
        loginRepository.login(toLoggedInUser(userUUID))
    }
}