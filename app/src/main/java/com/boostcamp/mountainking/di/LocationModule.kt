package com.boostcamp.mountainking.di

import android.content.Context
import com.boostcamp.mountainking.ui.tracking.LocationServiceManager
import com.boostcamp.mountainking.util.StringGetter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class LocationModule {

    @ViewModelScoped
    @Provides
    fun provideLocationServiceManager(@ApplicationContext context: Context): LocationServiceManager {
        return LocationServiceManager(context)
    }

    @ViewModelScoped
    @Provides
    fun provideStringGetter(@ApplicationContext context: Context): StringGetter {
        return StringGetter(context)
    }
}