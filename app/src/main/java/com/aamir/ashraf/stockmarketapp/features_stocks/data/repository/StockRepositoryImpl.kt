package com.aamir.ashraf.stockmarketapp.features_stocks.data.repository

import com.aamir.ashraf.stockmarketapp.core.utils.Resource
import com.aamir.ashraf.stockmarketapp.features_stocks.data.local.StockDao
import com.aamir.ashraf.stockmarketapp.features_stocks.data.local.StockDatabase
import com.aamir.ashraf.stockmarketapp.features_stocks.data.mapper.toCompanyListing
import com.aamir.ashraf.stockmarketapp.features_stocks.data.remote.ApiInterface
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.CompanyListing
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: ApiInterface,
    private val db:StockDatabase
):StockRepository {
    private val dao = db.dao
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
                response.byteStream()
            }catch (e:IOException){
                e.printStackTrace()
                emit(Resource.Error("please check your internet connection"))
            }
            catch (e:HttpException){
                e.printStackTrace()
                emit(Resource.Error(e.localizedMessage?:"parsing error ${e.code()}"))
            }

        }
    }
}