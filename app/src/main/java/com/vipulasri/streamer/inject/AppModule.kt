
package com.vipulasri.streamer.inject

import android.content.Context
import com.vipulasri.streamer.StreamerApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideContext(application: StreamerApplication): Context {
        return application.applicationContext
    }
}