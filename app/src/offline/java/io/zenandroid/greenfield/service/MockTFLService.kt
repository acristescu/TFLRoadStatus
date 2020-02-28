package io.zenandroid.greenfield.service

import androidx.annotation.VisibleForTesting
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.zenandroid.greenfield.model.Road
import io.zenandroid.greenfield.model.RoadError
import io.zenandroid.greenfield.model.RoadNetworkResponse
import io.zenandroid.greenfield.model.RoadNetworkResponse.ErrorResponse
import io.zenandroid.greenfield.model.RoadNetworkResponse.SuccessResponse
import io.zenandroid.greenfield.util.EspressoIdlingResource
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * created by acristescu
 */

private const val ERROR_ROAD = "ERROR_ROAD"

class MockTFLService @Inject constructor() : TFLService {

    private var mockResponse: List<Road>
    private var mockError = "The following road id is not recognised: ERROR_ROAD"
    private var delay = 0

    init {
        val gson = GsonBuilder().create()

        mockResponse = gson.fromJson(
                InputStreamReader(javaClass.classLoader!!.getResourceAsStream("mock_data.json")),
                object : TypeToken<ArrayList<Road>>() {}.type
        )
    }

    @VisibleForTesting
    fun setDelay(delay: Int) {
        this.delay = delay
    }

    override fun getRoad(id: String): Single<RoadNetworkResponse> {
        EspressoIdlingResource.increment()
        return if(id == ERROR_ROAD) {
            Single.just<RoadNetworkResponse>(ErrorResponse(RoadError(mockError)))
                    .delay(delay.toLong(), TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { EspressoIdlingResource.decrement() }
        } else {
            Single.just<RoadNetworkResponse>(SuccessResponse(mockResponse))
                    .delay(delay.toLong(), TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { EspressoIdlingResource.decrement() }
        }
    }
}
