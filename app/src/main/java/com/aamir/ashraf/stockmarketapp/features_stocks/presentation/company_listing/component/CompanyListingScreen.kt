package com.aamir.ashraf.stockmarketapp.features_stocks.presentation.company_listing.component

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aamir.ashraf.stockmarketapp.features_stocks.presentation.company_listing.CompanyListingEvent
import com.aamir.ashraf.stockmarketapp.features_stocks.presentation.company_listing.CompanyListingViewModel
import com.aamir.ashraf.stockmarketapp.features_stocks.presentation.destinations.CompanyInfoScreenDestination
import com.aamir.ashraf.stockmarketapp.features_stocks.presentation.destinations.CompanyListingScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination(start = true)
fun CompanyListingScreen(
    navigator: DestinationsNavigator,
    viewModel:CompanyListingViewModel = hiltViewModel()
){
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)

    val state = viewModel.state
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = state.searchQuery
            , onValueChange ={
                viewModel.onEvent(CompanyListingEvent.OnSearchQueryChange(it))
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
            ,
            placeholder = {
                Text(text = "Search...")
            },
            maxLines = 1,
            singleLine = true
            )
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onEvent(CompanyListingEvent.Refresh)
            }) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ){
                items(state.companies.size){index->
                    val company = state.companies[index]
                    CompanyItem(
                        companyListing = company,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                Log.e("aamir","company clicked ${company.symbol}")
                               navigator.navigate(
                                   CompanyInfoScreenDestination(symbol = company.symbol)
                               )
                            }
                            .padding(16.dp)
                    )
                    if(index<state.companies.size){
                        Divider(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }

                }

            }

        }

    }
}