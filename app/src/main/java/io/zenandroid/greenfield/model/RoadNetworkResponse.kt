package io.zenandroid.greenfield.model

sealed class RoadNetworkResponse {
    class SuccessResponse(val roadData: List<Road>): RoadNetworkResponse()
    class ErrorResponse(val error: RoadError): RoadNetworkResponse()
}