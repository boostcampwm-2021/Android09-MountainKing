package com.boostcamp.mountainking.di

import android.content.Context
import com.boostcamp.mountainking.data.Repository
import com.boostcamp.mountainking.data.RepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @ViewModelScoped
    @Provides
    fun provideRepository(@ApplicationContext context: Context): RepositoryInterface {
        return Repository.getInstance(context)
    }
}