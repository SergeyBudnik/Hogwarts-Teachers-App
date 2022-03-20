package com.bdev.hengschoolteacher.interactors.alerts.monitoring

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlertsMonitoringModule {
    @Singleton @Binds
    abstract fun bindAlertsMonitoringInteractor(
        alertsMonitoringInteractor: AlertsMonitoringInteractorImpl
    ): AlertsMonitoringInteractor

    @Singleton @Binds
    abstract fun bindAlertsMonitoringLessonsInteractor(
        alertsMonitoringLessonsInteractor: AlertsMonitoringLessonsInteractorImpl
    ): AlertsMonitoringLessonsInteractor

    @Singleton @Binds
    abstract fun bindAlertsMonitoringStudentsInteractor(
        alertsMonitoringStudentsInteractor: AlertsMonitoringStudentsInteractorImpl
    ): AlertsMonitoringStudentsInteractor

    @Singleton @Binds
    abstract fun bindAlertsMonitoringTeachersInteractor(
        alertsMonitoringTeachersInteractor: AlertsMonitoringTeachersInteractorImpl
    ): AlertsMonitoringTeachersInteractor
}