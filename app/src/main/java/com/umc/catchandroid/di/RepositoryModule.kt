package com.umc.catchandroid.di

import com.umc.catchandroid.data.repository.MockNoticeRepositoryImpl
import com.umc.catchandroid.data.repository.MockUserRepositoryImpl
import com.umc.catchandroid.domain.repository.NoticeRepository
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
}