package io.zenandroid.greenfield.roadstatus

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nhaarman.mockitokotlin2.argThat
import io.reactivex.Single
import io.zenandroid.greenfield.model.Road
import io.zenandroid.greenfield.model.RoadError
import io.zenandroid.greenfield.model.RoadNetworkResponse.ErrorResponse
import io.zenandroid.greenfield.model.RoadNetworkResponse.SuccessResponse
import io.zenandroid.greenfield.service.TFLService
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.io.InputStreamReader

/**
 * Created by alex on 27/01/2018.
 */

private const val ERROR_ROAD = "ERROR_ROAD"

class RoadStatusPresenterTest {

    @Mock
    lateinit var view: RoadStatusContract.View

    @Mock
    lateinit var service: TFLService


    private lateinit var presenter: RoadStatusPresenter
    private lateinit var fakeResponse: SuccessResponse
    private lateinit var fakeError: ErrorResponse

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = RoadStatusPresenter(view, service)

        val gson = GsonBuilder().create()
        fakeResponse = SuccessResponse(
                gson.fromJson<List<Road>>(
                        InputStreamReader(javaClass.classLoader!!.getResourceAsStream("mock_data.json")),
                        object : TypeToken<ArrayList<Road>>() {}.type
                )
        )

        fakeError = ErrorResponse(RoadError("ERROR_TEXT"))

        `when`(service.getRoad(argThat { this != ERROR_ROAD })).thenReturn(Single.just(fakeResponse))
        `when`(service.getRoad(argThat { this == ERROR_ROAD })).thenReturn(Single.just(fakeError))
    }

    @Test
    fun whenValidRoadIsSpecified_thenRoadDetailsAreShown() {
        val expectedData = fakeResponse.roadData[0]

        presenter.subscribe()
        presenter.onSearchTextChange("A2")
        presenter.onSearchPressed()

        verify(view).showProgressDialog()
        verify(view).showRoadDetails("${expectedData.displayName}\nRoad Status: ${expectedData.statusSeverity}\nRoad Status Description: ${expectedData.statusSeverityDescription}")
        verify(view).dismissProgressDialog()
    }

    @Test
    fun whenInvalidRoadIsSpecified_thenErrorIsShown() {
        val expectedData = fakeError.error.message!!

        presenter.subscribe()
        presenter.onSearchTextChange(ERROR_ROAD)
        presenter.onSearchPressed()

        verify(view).showProgressDialog()
        verify(view).showRoadDetails(expectedData)
        verify(view).dismissProgressDialog()
    }

    @Test
    fun whenTextIsEntered_thenButtonIsEnabled() {
        presenter.subscribe()
        presenter.onSearchTextChange("aaaa")

        verify(view).setSearchButtonEnabled(true)
    }

    @Test
    fun whenTextIsEmpty_thenButtonIsDisabled() {
        presenter.subscribe()
        presenter.onSearchTextChange("aaaa")
        presenter.onSearchTextChange("")

        verify(view).setSearchButtonEnabled(false)
    }

}
