package com.autospare.module

import com.autospare.service.LogService
import com.autospare.service.LogServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Author: Hari K
 * Date: 21/11/2023.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideLogService(impl: LogServiceImpl): LogService

}