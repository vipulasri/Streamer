package com.vipulasri.streamer.inject;

import com.vipulasri.streamer.ui.details.PostDetailsActivity;
import com.vipulasri.streamer.ui.home.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract PostDetailsActivity postDetailsActivity();
}
