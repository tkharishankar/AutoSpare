package com.autospare.module

import com.autospare.service.UserPreference
import com.autospare.service.UserPreferenceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Author: Senthil
 * Date: 23/11/2023.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class UserPreferenceModule {
    @Binds
    abstract fun bindUserPreferences(impl: UserPreferenceImpl): UserPreference
}