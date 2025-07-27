package br.com.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.home.domain.repository.HomeRepository
import br.com.home.ui.state.HomeActions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _actions = Channel<HomeActions>(Channel.BUFFERED)
    val actions = _actions.receiveAsFlow()

    fun logout() = viewModelScope.launch {
        homeRepository.logout()
        _actions.send(HomeActions.NavigateToLogin)
    }
}