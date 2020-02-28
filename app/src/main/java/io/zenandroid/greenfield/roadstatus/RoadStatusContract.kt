package io.zenandroid.greenfield.roadstatus

/**
 * Created by alex on 25/01/2018.
 */

interface RoadStatusContract {

    interface View : io.zenandroid.greenfield.base.View<Presenter> {
        fun setSearchButtonEnabled(enabled: Boolean)
        fun showRoadDetails(details: String)
    }

    interface Presenter : io.zenandroid.greenfield.base.Presenter {
        fun onSearchPressed()
        fun onSearchTextChange(searchText: String)
    }
}
