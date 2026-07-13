package com.umc.catchandroid.di

import com.umc.catchandroid.data.repository.MockCalendarRepositoryImpl
import com.umc.catchandroid.data.repository.MockNoticeRepositoryImpl
import com.umc.catchandroid.data.repository.MockSearchRepositoryImpl
import com.umc.catchandroid.data.repository.MockUserRepositoryImpl
import com.umc.catchandroid.domain.repository.CalendarRepository
import com.umc.catchandroid.domain.repository.NoticeRepository
import com.umc.catchandroid.domain.repository.SearchRepository
import com.umc.catchandroid.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindNoticeRepository(
        impl: MockNoticeRepositoryImpl
    ): NoticeRepository

    @Binds
    abstract fun bindUserRepository(
        impl: MockUserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindSearchRepository(
        impl: MockSearchRepositoryImpl
    ): SearchRepository

    @Binds
    abstract fun bindCalendarRepository(
        impl: MockCalendarRepositoryImpl
    ): CalendarRepository
}