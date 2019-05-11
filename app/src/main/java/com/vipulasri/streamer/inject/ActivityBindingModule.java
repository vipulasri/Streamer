package com.vipulasri.streamer.inject;

import com.vipulasri.streamer.ui.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();
}
