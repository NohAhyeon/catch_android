package com.umc.catchandroid.di

import com.umc.catchandroid.data.repository.CalendarRepositoryImpl
import com.umc.catchandroid.data.repository.SearchRepositoryImpl
import com.umc.catchandroid.data.repository.NoticeRepositoryImpl
import com.umc.catchandroid.data.repository.SpecRepositoryImpl
import com.umc.catchandroid.data.repository.UniversityRepositoryImpl
import com.umc.catchandroid.data.repository.UserRepositoryImpl
import com.umc.catchandroid.domain.repository.CalendarRepository
import com.umc.catchandroid.domain.repository.NoticeRepository
import com.umc.catchandroid.domain.repository.SearchRepository
import com.umc.catchandroid.domain.repository.SpecRepository
import com.umc.catchandroid.domain.repository.UniversityRepository
import com.umc.catchandroid.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.umc.catchandroid.data.repository.NotificationRepositoryImpl
import com.umc.catchandroid.domain.repository.NotificationRepository
import com.umc.catchandroid.data.repository.SupportRepositoryImpl
import com.umc.catchandroid.domain.repository.SupportRepository




@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindNoticeRepository(
        impl: NoticeRepositoryImpl
    ): NoticeRepository

    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindSearchRepository(
        impl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    abstract fun bindCalendarRepository(
        impl: CalendarRepositoryImpl
    ): CalendarRepository

    @Binds
    abstract fun bindUniversityRepository(
        impl: UniversityRepositoryImpl
    ): UniversityRepository

    @Binds
    abstract fun bindSpecRepository(
        impl: SpecRepositoryImpl
    ): SpecRepository

    @Binds
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    abstract fun bindSupportRepository(
        impl: SupportRepositoryImpl
    ): SupportRepository
}