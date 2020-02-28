package io.zenandroid.greenfield.service

import com.google.gson.GsonBuilder
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.zenandroid.greenfield.BuildConfig
import io.zenandroid.greenfield.api.TFLApi
import io.zenandroid.greenfield.model.RoadError
import io.zenandroid.greenfield.model.RoadNetworkResponse
import io.zenandroid.greenfield.model.RoadNetworkResponse.*
import io.zenandroid.greenfield.util.EspressoIdlingResource
import retrofit2.HttpException
import java.io.InputStreamReader
import javax.inject.Inject

class TFLServiceImpl @Inject
constructor(private val tflApi: TFLApi) : TFLService {

    private val errorGson = GsonBuilder().create()

    override fun getRoad(id: String): Single<RoadNetworkResponse> {
        EspressoIdlingResource.increment()
        return tflApi.getRoad(id, BuildConfig.TFL_APP_ID, BuildConfig.TFL_APP_KEY)
                .map<RoadNetworkResponse>(::SuccessResponse)
                .onErrorReturn (this::parseException)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { EspressoIdlingResource.decrement() }
    }

    private fun parseException(t: Throwable): RoadNetworkResponse {
        return if (
                t is HttpException
                && t.code() == 404
                && t.response()?.errorBody()?.contentType()?.subtype() == "json"
        ) {
            val error = t.response()?.errorBody()?.byteStream()?.let {
                errorGson.fromJson(InputStreamReader(it), RoadError::class.java)
            } ?: RoadError(null)

            ErrorResponse(error)
        } else {
            ErrorResponse(RoadError(t.message))
        }
    }

    companion object {

        private val TAG = TFLServiceImpl::class.java.simpleName
    }
}
