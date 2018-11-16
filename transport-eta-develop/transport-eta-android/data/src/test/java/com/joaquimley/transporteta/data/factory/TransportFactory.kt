package com.joaquimley.transporteta.data.factory

import com.joaquimley.transporteta.data.factory.DataFactory.Factory.randomInt
import com.joaquimley.transporteta.data.factory.DataFactory.Factory.randomUuid
import com.joaquimley.transporteta.data.model.TransportEntity
import com.joaquimley.transporteta.domain.model.Transport

/**
 * Factory class for [Transport] related instances
 */
class TransportFactory {

    companion object Factory {

        fun makeTransportList(count: Int, isFavorite: Boolean = false, type: String = randomUuid()): List<Transport> {
            val transports = mutableListOf<Transport>()
            repeat(count) {
                transports.add(makeTransport(isFavorite, type))
            }
            return transports
        }

        fun makeTransport(isFavorite: Boolean = false, type: String = randomUuid(), code: Int = randomInt(), id: String = randomUuid()): Transport {
            return Transport(id, randomUuid(), code, randomUuid(), isFavorite, type)
        }

        fun makeTransportEntityList(count: Int, isFavorite: Boolean = false, type: String = randomUuid()): List<TransportEntity> {
            val transports = mutableListOf<TransportEntity>()
            repeat(count) {
                transports.add(makeTransportEntity(isFavorite, type))
            }
            return transports
        }

        fun makeTransportEntity(isFavorite: Boolean = false, type: String = randomUuid(), code: Int = randomInt(), id: String = randomUuid()): TransportEntity {
            return TransportEntity(id, randomUuid(), code, randomUuid(), isFavorite, type)
        }

    }

}