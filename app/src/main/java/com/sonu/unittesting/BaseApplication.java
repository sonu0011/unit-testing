package com.sonu.unittesting;

import com.sonu.unittesting.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class BaseApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends dagger.android.support.DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
