package com.vipulasri.streamer

import com.vipulasri.streamer.inject.DaggerAppComponent
import com.vipulasri.streamer.inject.NetworkModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class StreamerApplication : DaggerApplication() {

    val TAG = StreamerApplication::class.java.simpleName

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    companion object {
        lateinit var instance: StreamerApplication
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder()
                .application(this)
                .networkModule(NetworkModule(BuildConfig.API_BASE_URL))
                .build()
        appComponent.inject(this)
        return appComponent
    }
}
