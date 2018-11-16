package com.joaquimley.transporteta.data

import com.joaquimley.transporteta.data.mapper.DataTransportMapper
import com.joaquimley.transporteta.data.store.TransportDataStore
import com.joaquimley.transporteta.domain.model.Transport
import com.joaquimley.transporteta.domain.repository.TransportRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransportRepositoryImpl @Inject constructor(private val transportDataStore: TransportDataStore,
                                                  private val mapper: DataTransportMapper) : TransportRepository {

    override fun requestTransportEta(transportCode: Int): Observable<Transport> {
        return transportDataStore.requestTransportEta(transportCode).map { mapper.toModel(it) }
    }

    override fun cancelTransportEtaRequest(transportCode: Int?): Completable {
        return transportDataStore.cancelTransportEtaRequest(transportCode)
    }

    override fun saveTransport(transport: Transport): Completable {
        return transportDataStore.saveTransport(mapper.toEntity(transport))
    }

    override fun deleteTransport(transportId: String): Completable {
        return transportDataStore.deleteTransport(transportId)
    }

    override fun getTransport(transportId: String): Observable<Transport> {
        return transportDataStore.getTransport(transportId).map { mapper.toModel(it) }
    }

    override fun getAll(): Flowable<List<Transport>> {
        return transportDataStore.getAll().map { mapper.toModel(it) }
    }
}