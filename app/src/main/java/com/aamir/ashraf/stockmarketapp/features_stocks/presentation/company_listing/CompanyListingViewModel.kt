package com.aamir.ashraf.stockmarketapp.features_stocks.presentation.company_listing

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aamir.ashraf.stockmarketapp.core.utils.Resource
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val stockRepository: StockRepository
):ViewModel() {
     var state by mutableStateOf(CompanyListingState())
    private var searchJob:Job?=null
    init {
        getCompanyListing()
    }

    fun onEvent(event: CompanyListingEvent){
        when(event){
            is CompanyListingEvent.Refresh ->{
                getCompanyListing(fetchFromRemote = true)

            }
            is CompanyListingEvent.OnSearchQueryChange ->{
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListing()
                }

            }


        }
    }

    //fetch data
    fun getCompanyListing(
        query:String = state.searchQuery.lowercase(),
        fetchFromRemote:Boolean = false

    ){
        viewModelScope.launch {
            stockRepository.getCompanyListing(query=query, fetchFromRemote = fetchFromRemote)
                .collect{result->
                    when(result){
                      is Resource.Loading ->{
                          state = state.copy(
                              isLoading = result.isLoading
                          )
                      }
                        is Resource.Success ->{
                            result.data?.let {listings->
                                state = state.copy(
                                    companies = listings
                                )

                            }

                        }
                        is Resource.Error ->{
                            Unit
                            Log.e("viewmodel","some error occured")

                        }
                    }

                }
        }
    }
}