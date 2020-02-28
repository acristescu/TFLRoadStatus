package io.zenandroid.greenfield.dagger

import dagger.Component
import io.zenandroid.greenfield.roadstatus.RoadStatusActivity

/**
 * Created by acristescu on 22/06/2017.
 */

@Component(modules = [AppModule::class, TFLServiceModule::class])
interface AppComponent {

    fun inject(activity: RoadStatusActivity)

}
