package com.bdev.hengschoolteacher.interactors.alerts.profile

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlertsProfileModule {
    @Singleton @Binds
    abstract fun bindAlertsProfileInteractor(
        alertsProfileInteractor: AlertsProfileInteractorImpl
    ): AlertsProfileInteractor
}