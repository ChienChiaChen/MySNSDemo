package com.chiachen.mysnsdemo.data.di

import android.content.Context
import androidx.room.Room
import com.chiachen.mysnsdemo.data.local.dao.PendingPostDao
import com.chiachen.mysnsdemo.data.local.dao.PostDao
import com.chiachen.mysnsdemo.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "my_sns_demo_db"
        ).build()
    }

    @Provides
    fun providePostDao(db: AppDatabase): PostDao = db.postDao()

    @Provides
    fun providePendingPostDao(db: AppDatabase): PendingPostDao = db.pendingPostDao()
}
