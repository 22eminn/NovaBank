package com.example.novabank.feature.transfer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novabank.core.util.parseAmountToMinor
import com.example.novabank.core.util.sanitizeAmountInput
import com.example.novabank.feature.home.domain.HomeRepository
import com.example.novabank.feature.transfer.domain.TransferRepository
import com.example.novabank.feature.transfer.domain.ValidateTransferUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val transferRepository: TransferRepository,
    private val validateTransfer: ValidateTransferUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()

    init {
        loadBalance()
    }

    private fun loadBalance() {
        viewModelScope.launch {
            try {
                val account = homeRepository.getAccount()
                _uiState.update { it.copy(balanceMinor = account.balanceMinor) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Balans yüklənmədi") }
            }
        }
    }

    fun onCardChange(input: String) {
        val digits = input.filter { it.isDigit() }.take(16)
        _uiState.update { it.copy(cardInput = digits, cardError = false, errorMessage = null) }
    }

    fun onAmountChange(input: String) {
        _uiState.update {
            it.copy(
                amountInput = sanitizeAmountInput(input),
                amountError = null,
                errorMessage = null,
            )
        }
    }

    fun onSubmit() {
        val state = _uiState.value
        if (state.isSubmitting) return // ikiqat basışdan qoruma

        val amountMinor = parseAmountToMinor(state.amountInput)
        val validation = validateTransfer(state.cardInput, amountMinor, state.balanceMinor)
        if (!validation.isValid || amountMinor == null) {
            _uiState.update {
                it.copy(cardError = validation.cardError, amountError = validation.amountError)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            try {
                val receipt = transferRepository.transfer(state.cardInput, amountMinor)
                _uiState.update { it.copy(isSubmitting = false, receipt = receipt) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = "Köçürmə alınmadı, yenidən cəhd et")
                }
            }
        }
    }
}