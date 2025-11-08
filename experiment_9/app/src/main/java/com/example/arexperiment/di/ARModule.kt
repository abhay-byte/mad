package com.example.arexperiment.di

import android.content.Context
import com.example.arexperiment.domain.ar.ARManager
import com.example.arexperiment.domain.ar.ObjectManager
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
    fun provideARManager(@ApplicationContext context: Context): ARManager = ARManager(context)

    @Provides
    @Singleton
    fun provideObjectManager(@ApplicationContext context: Context): ObjectManager =
            ObjectManager(context)
}
