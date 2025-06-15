package com.chiachen.mysnsdemo.data.di

import com.chiachen.mysnsdemo.data.repository.FirebaseRepositoryImpl
import com.chiachen.mysnsdemo.data.repository.PostRepositoryImpl
import com.chiachen.mysnsdemo.domain.FirebaseRepository
import com.chiachen.mysnsdemo.domain.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPostRepository(
        impl: PostRepositoryImpl
    ): PostRepository

    @Binds
    abstract fun bindFirebaseRepository(
        impl: FirebaseRepositoryImpl
    ): FirebaseRepository
}
