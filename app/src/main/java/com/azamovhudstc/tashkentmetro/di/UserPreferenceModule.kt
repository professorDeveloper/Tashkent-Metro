package com.azamovhudstc.tashkentmetro.di

import android.content.Context
import com.azamovhudstc.tashkentmetro.domain.preference.UserPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserPreferenceModule {

    @Singleton
    @Provides
    fun provideUserPreferenceManager(@ApplicationContext context: Context): UserPreferenceManager {
        return UserPreferenceManager(context)
    }
}