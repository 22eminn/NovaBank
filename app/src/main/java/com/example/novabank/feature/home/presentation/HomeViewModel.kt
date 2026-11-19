package com.example.novabank.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novabank.feature.home.domain.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    init {
        load()
    }

    fun load() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val state: Flow<HomeUiState> = combine(
                repository.observeAccount(),
                repository.observeRecentTransactions(),
            ) { account, transactions ->
                HomeUiState.Success(account, transactions)
            }
            state
                .catch { emit(HomeUiState.Error("Məlumat yüklənmədi")) }
                .collect { _uiState.value = it }
        }
    }
}