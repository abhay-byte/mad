package com.example.arexperiment.di

import android.content.Context
import com.example.arexperiment.domain.ar.ARManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ARModule {

    @Provides
    @Singleton
    fun provideARManager(
        @ApplicationContext context: Context
    ): ARManager = ARManager(context)
}