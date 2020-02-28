package io.zenandroid.greenfield.service

import io.reactivex.Single
import io.zenandroid.greenfield.model.RoadNetworkResponse

/**
 * created by acristescu
 */

interface TFLService {
    fun getRoad(id: String): Single<RoadNetworkResponse>
}
