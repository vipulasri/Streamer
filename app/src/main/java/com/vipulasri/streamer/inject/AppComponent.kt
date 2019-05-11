
package com.vipulasri.streamer.inject

import android.app.Application
import com.vipulasri.streamer.inject.AppModule
import com.vipulasri.streamer.inject.NetworkModule
import com.vipulasri.streamer.inject.ViewModelModule
import com.vipulasri.streamer.StreamerApplication
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton
import dagger.BindsInstance

@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        ActivityBindingModule::class,
        ViewModelModule::class))
interface AppComponent : AndroidInjector<StreamerApplication> {

    override fun inject(instance: StreamerApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent

        fun networkModule(networkModule: NetworkModule): Builder
    }
}