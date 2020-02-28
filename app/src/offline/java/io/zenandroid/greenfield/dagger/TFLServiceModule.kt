package io.zenandroid.greenfield.dagger

import dagger.Binds
import dagger.Module
import io.zenandroid.greenfield.service.MockTFLService
import io.zenandroid.greenfield.service.TFLService

/**
 * created by acristescu
 */
@Module
abstract class TFLServiceModule {

    @Binds
    internal abstract fun provideTFLService(bbcService: MockTFLService): TFLService
}
