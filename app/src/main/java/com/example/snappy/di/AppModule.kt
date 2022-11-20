package com.example.snappy.di

import android.content.Context
import com.example.snappy.base.PrivateSharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): PrivateSharedPrefManager {
        return PrivateSharedPrefManager(context)
    }
}
