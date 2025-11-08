package com.example.arexperiment.di

import android.content.Context
import com.example.arexperiment.domain.ar.ARManager
import com.example.arexperiment.domain.ar.ObjectManager
import com.example.arexperiment.domain.ar.cloud.CloudAnchorManager
import com.example.arexperiment.domain.ar.image.AugmentedImageManager
import com.example.arexperiment.domain.ar.pointcloud.PointCloudManager
import com.example.arexperiment.domain.ar.environment.EnvironmentManager
import com.example.arexperiment.domain.ar.camera.GltfCameraManager
import com.google.android.filament.Engine
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

    @Provides
    @Singleton
    fun provideEngine(): Engine = Engine.create()

    @Provides
    @Singleton
    fun provideCloudAnchorManager(
        @ApplicationContext context: Context
    ): CloudAnchorManager = CloudAnchorManager(context)

    @Provides
    @Singleton
    fun provideAugmentedImageManager(
        @ApplicationContext context: Context
    ): AugmentedImageManager = AugmentedImageManager(context)

    @Provides
    @Singleton
    fun providePointCloudManager(): PointCloudManager = PointCloudManager()

    @Provides
    @Singleton
    fun provideEnvironmentManager(
        @ApplicationContext context: Context,
        engine: Engine
    ): EnvironmentManager = EnvironmentManager(context, engine)

    @Provides
    @Singleton
    fun provideGltfCameraManager(
        engine: Engine
    ): GltfCameraManager = GltfCameraManager(engine)
}
