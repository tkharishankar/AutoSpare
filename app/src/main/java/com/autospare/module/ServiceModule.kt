package com.autospare.module

import com.autospare.service.AccountService
import com.autospare.service.AccountServiceImpl
import com.autospare.service.LogService
import com.autospare.service.LogServiceImpl
import com.autospare.service.StorageService
import com.autospare.service.StorageServiceImpl
import com.autospare.service.UserPreference
import com.autospare.service.UserPreferenceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Author: Senthil
 * Date: 21/11/2023.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideLogService(impl: LogServiceImpl): LogService
    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService
    @Binds
    abstract fun provideUserPreferences(impl: UserPreferenceImpl): UserPreference
}