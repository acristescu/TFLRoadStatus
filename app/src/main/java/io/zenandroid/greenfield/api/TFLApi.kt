package io.zenandroid.greenfield.api

import io.reactivex.Single
import io.zenandroid.greenfield.BuildConfig
import io.zenandroid.greenfield.model.Road
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TFLApi {
    @GET("Road/{roadId}")
    fun getRoad(
            @Path("roadId") id: String,
            @Query("app_id") app_id: String,
            @Query("app_key") app_key: String
    ): Single<List<Road>>
}