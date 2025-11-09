package com.example.experiment5.di

import android.content.Context
import com.example.experiment5.data.repository.LocationRepository
import com.google.android.gms.location.LocationServices

object AppModule {
    fun provideLocationRepository(context: Context): LocationRepository {
        return LocationRepository(
            context,
            LocationServices.getFusedLocationProviderClient(context)
        )
    }
}