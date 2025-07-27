package br.com.home.ui.dvdview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.desafiomercantil.core.remote.Result
import br.com.home.R
import br.com.home.domain.mapper.toDvdMessages
import br.com.home.domain.model.Message
import br.com.home.domain.model.MessageResult
import br.com.home.domain.repository.MessageRepository
import br.com.home.ui.components.DvdMessage
import kotlinx.coroutines.launch

class DvdViewModel(
    private val realMessageRepository: MessageRepository,
    private val mockMessageRepository: MessageRepository
) : ViewModel() {

    private val _messageResult = MutableLiveData<MessageResult>()
    val messageResult: LiveData<MessageResult> = _messageResult

    private val _dvdMessageList = MutableLiveData<List<DvdMessage>>()
    val dvdMessageList: LiveData<List<DvdMessage>> = _dvdMessageList

    private var isRealRequest: Boolean = true

    fun getMessage(lastId: Int) {
        viewModelScope.launch {
            val result =
                if (isRealRequest) {
                    realMessageRepository.getMessage(lastId)
                } else {
                    mockMessageRepository.getMessage()
                }
            if (result is Result.Success) {
                _messageResult.value = MessageResult(success = result.data)
            } else {
                _messageResult.value = MessageResult(error = R.string.message_error)
            }
        }
    }

    fun saveMessage(message: Message) {
        realMessageRepository.saveLocalMessage(message)
    }

    fun readAllMessagesLocal() {
        _dvdMessageList.value = realMessageRepository.readAllMessages().toDvdMessages()
    }

    fun toggleRealRequest() {
        this.isRealRequest = !this.isRealRequest
    }
}