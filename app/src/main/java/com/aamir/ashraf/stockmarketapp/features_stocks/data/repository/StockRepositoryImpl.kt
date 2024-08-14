package com.aamir.ashraf.stockmarketapp.features_stocks.data.repository

import com.aamir.ashraf.stockmarketapp.core.utils.Resource
import com.aamir.ashraf.stockmarketapp.features_stocks.data.csv.CSVParser
import com.aamir.ashraf.stockmarketapp.features_stocks.data.csv.CompanyListingParser
import com.aamir.ashraf.stockmarketapp.features_stocks.data.local.StockDao
import com.aamir.ashraf.stockmarketapp.features_stocks.data.local.StockDatabase
import com.aamir.ashraf.stockmarketapp.features_stocks.data.mapper.toCompanyListing
import com.aamir.ashraf.stockmarketapp.features_stocks.data.mapper.toCompanyListingEntity
import com.aamir.ashraf.stockmarketapp.features_stocks.data.remote.ApiInterface
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.CompanyListing
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

class StockRepositoryImpl (
    private val api: ApiInterface,
    private val dao: StockDao,
    private val companyListingParser: CSVParser<CompanyListing>
):StockRepository {

    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val localListing = dao.searchCompanyListing(query)
            emit(Resource.Success(data = localListing.map {
                it.toCompanyListing()
            }))

            //if we want to make an api call
            //there is few condition to make the api call
            /*
            ToDo 1>if we make swipe to refresh
            ToDo 2>if localListing list is empty
             */

            val isDbIsEmpty = localListing.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbIsEmpty && !fetchFromRemote

            //Todo very first we only load from api because because very first time local db is empty
            //todo then after ward we load from cache(local db)

            if(shouldJustLoadFromCache){
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListing = try {
               val response = api.getListings()
                companyListingParser.parse(response.byteStream())
            }catch (e:IOException){
                e.printStackTrace()
                emit(Resource.Error("please check your internet connection"))
                null  //get null from api
            }
            catch (e:HttpException){
                e.printStackTrace()
                emit(Resource.Error(e.localizedMessage?:"parsing error ${e.code()}"))
                null  //get null from api
            }
            remoteListing?.let {listing->
                //todo single source of truth is important
                //todo we always load data to ui from local not from api
                dao.clearCompanyListings()
                dao.insertCompanyListings(listing.map { it.toCompanyListingEntity() })
                emit(Resource.Success(
                    data = dao.searchCompanyListing("")   //load data from local
                        .map {
                            it.toCompanyListing()
                        }
                ))
                emit(Resource.Loading(false))
            }

        }
    }
}