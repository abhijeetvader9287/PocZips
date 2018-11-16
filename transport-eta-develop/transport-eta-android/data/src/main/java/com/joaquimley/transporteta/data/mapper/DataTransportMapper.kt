package com.joaquimley.transporteta.data.mapper

import com.joaquimley.transporteta.data.model.TransportEntity
import com.joaquimley.transporteta.domain.model.Transport

class DataTransportMapper {

    fun toEntity(from: List<Transport>): List<TransportEntity> {
        return from.map { toEntity(it) }
    }

    fun toEntity(from: Transport): TransportEntity {
        return TransportEntity(from.id, from.name, from.code, from.latestEta, from.isFavorite, from.type)
    }

    fun toModel(from: List<TransportEntity>): List<Transport> {
        return from.map { toModel(it) }
    }

    fun toModel(from: TransportEntity): Transport {
        return Transport(from.id, from.name, from.code, from.latestEta, from.isFavorite, from.type)
    }

}