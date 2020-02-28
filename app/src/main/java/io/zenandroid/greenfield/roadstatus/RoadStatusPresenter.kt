package io.zenandroid.greenfield.roadstatus


import io.zenandroid.greenfield.base.BasePresenter
import io.zenandroid.greenfield.model.RoadNetworkResponse
import io.zenandroid.greenfield.model.RoadNetworkResponse.*
import io.zenandroid.greenfield.service.TFLService

/**
 * Created by alex on 25/01/2018.
 */

class RoadStatusPresenter(private val view: RoadStatusContract.View, private val tflService: TFLService) : BasePresenter(view), RoadStatusContract.Presenter {

    private var currentRoadId = ""

    override fun onSearchPressed() {
        view.showProgressDialog()
        addDisposable(
            tflService.getRoad(currentRoadId)
                    .subscribe(
                            this::onRoadDataResponse,
                            this::onError // note this would still get the unexpected errors, such as internet being down
                    )
        )
    }

    override fun onSearchTextChange(searchText: String) {
        currentRoadId = searchText
        view.setSearchButtonEnabled(!currentRoadId.isBlank())
    }

    override fun subscribe() {
    }

    private fun onRoadDataResponse(response: RoadNetworkResponse) {
        view.dismissProgressDialog()
        when(response) {
            is SuccessResponse -> {
                if(response.roadData.isEmpty()) {
                    view.showErrorMessage("Received empty message")
                } else {
                    val roadData = response.roadData[0]
                    view.showRoadDetails("${roadData.displayName}\nRoad Status: ${roadData.statusSeverity}\nRoad Status Description: ${roadData.statusSeverityDescription}")
                }
            }
            is ErrorResponse -> {
                view.showRoadDetails(response.error.message ?: "Generic error")
            }
        }
    }

}
