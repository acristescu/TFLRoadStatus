package io.zenandroid.greenfield.roadstatus

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import io.zenandroid.greenfield.R
import io.zenandroid.greenfield.base.BaseActivity
import io.zenandroid.greenfield.dagger.Injector
import io.zenandroid.greenfield.service.TFLService
import kotlinx.android.synthetic.main.activity_road_status.*
import javax.inject.Inject

class RoadStatusActivity : BaseActivity(R.layout.activity_road_status), RoadStatusContract.View {

    private lateinit var presenter: RoadStatusContract.Presenter

    @Inject
    lateinit var tflService: TFLService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.get().inject(this)

        searchButton.setOnClickListener { presenter.onSearchPressed() }
        roadTextEdit.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                presenter.onSearchTextChange(s.toString())
            }

        })
        presenter = RoadStatusPresenter(this, tflService)
    }

    override fun setSearchButtonEnabled(enabled: Boolean) {
        searchButton.isEnabled = enabled
    }

    override fun showRoadDetails(details: String) {
        detailsTextView.text = details
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }
}
