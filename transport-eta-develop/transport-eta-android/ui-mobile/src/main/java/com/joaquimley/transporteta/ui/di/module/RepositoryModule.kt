package com.joaquimley.transporteta.ui.di.module

import com.joaquimley.transporteta.domain.repository.FavoritesRepository
import com.joaquimley.transporteta.data.FavoritesRepositoryImpl
import com.joaquimley.transporteta.domain.repository.TransportRepository
import com.joaquimley.transporteta.data.TransportRepositoryImpl
import com.joaquimley.transporteta.data.mapper.DataTransportMapper
import com.joaquimley.transporteta.data.store.TransportDataStore
import com.joaquimley.transporteta.ui.di.scope.PerApplication
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    @PerApplication
    fun provideFavoritesRepository(transportDataStore: TransportDataStore, dataTransportMapper: DataTransportMapper): FavoritesRepository {
        return FavoritesRepositoryImpl(transportDataStore, dataTransportMapper)
    }

    @Provides
    @PerApplication
    fun provideTransportRepository(transportDataStore: TransportDataStore, dataTransportMapper: DataTransportMapper): TransportRepository {
        return TransportRepositoryImpl(transportDataStore,dataTransportMapper)
    }


}