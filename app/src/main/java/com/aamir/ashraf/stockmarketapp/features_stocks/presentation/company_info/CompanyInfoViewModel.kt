package com.aamir.ashraf.stockmarketapp.features_stocks.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aamir.ashraf.stockmarketapp.core.utils.Resource
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle  //this is the way to get navigation args directly
    //in viewModel without explicitly passing them
    ,
    private val stockRepository: StockRepository
):ViewModel() {
    var state by mutableStateOf(CompanyInfoState())
    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol")?:return@launch
            state = state.copy(isLoading = true)

            val companyInfoResult = async { stockRepository.getCompanyInfo(symbol) }
            val intraDayInfoResult =async { stockRepository.getIntraDayInfo(symbol) }
            when(val result = companyInfoResult.await()){
                is Resource.Success ->{
                    state = state.copy(
                        companyInfo = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error ->{
                    state = state.copy(
                        companyInfo = null,
                        isLoading = false,
                        error = null
                    )

                }
                is Resource.Loading ->Unit
            }

            when(val result = intraDayInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        stockInfos = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        companyInfo = null
                    )
                }
                else -> Unit
            }
        }
    }

}