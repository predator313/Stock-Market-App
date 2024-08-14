package com.aamir.ashraf.stockmarketapp.di

import android.app.Application
import androidx.room.Room
import com.aamir.ashraf.stockmarketapp.core.BASE_URL
import com.aamir.ashraf.stockmarketapp.core.DB_NAME
import com.aamir.ashraf.stockmarketapp.features_stocks.data.csv.CSVParser
import com.aamir.ashraf.stockmarketapp.features_stocks.data.csv.CompanyListingParser
import com.aamir.ashraf.stockmarketapp.features_stocks.data.local.StockDao
import com.aamir.ashraf.stockmarketapp.features_stocks.data.local.StockDatabase
import com.aamir.ashraf.stockmarketapp.features_stocks.data.remote.ApiInterface
import com.aamir.ashraf.stockmarketapp.features_stocks.data.repository.StockRepositoryImpl
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.CompanyListing
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.repository.StockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideStockApi():ApiInterface{
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }
    @Provides
    @Singleton
    fun provideStockDataBase(context:Application):StockDatabase{
        return Room
            .databaseBuilder(
                context,
                StockDatabase::class.java,
                DB_NAME
            )
            .build()
    }
    @Provides
    @Singleton
    fun provideStockDao(db:StockDatabase):StockDao{
        return db.dao
    }
    @Provides
    @Singleton
    fun provideCSVParser():CSVParser<CompanyListing>{
        return CompanyListingParser()

    }
    @Provides
    @Singleton
    fun provideStockRepository(api:ApiInterface,dao: StockDao,csvParser: CSVParser<CompanyListing>):StockRepository{
        return StockRepositoryImpl(api=api,dao = dao, companyListingParser = csvParser)
    }
}