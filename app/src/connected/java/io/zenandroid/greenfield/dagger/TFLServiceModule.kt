package io.zenandroid.greenfield.dagger

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.zenandroid.greenfield.BuildConfig
import io.zenandroid.greenfield.api.TFLApi
import io.zenandroid.greenfield.service.TFLService
import io.zenandroid.greenfield.service.TFLServiceImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * created by acristescu
 */
@Module
class TFLServiceModule {

    @Provides
    internal fun provideTFLApi(): TFLApi {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okClientBuilder = OkHttpClient.Builder()
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(logging)

        val gson = GsonBuilder()
                .create()

        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TFLApi::class.java)
    }

    @Provides
    internal fun provideTFLService(service: TFLServiceImpl): TFLService {
        return service
    }

    companion object {

        private val TIMEOUT = 15
    }

}
