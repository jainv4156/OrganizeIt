package com.example.organizeit.di

import android.app.Application
import com.example.organizeit.data.Database
import com.example.organizeit.contentScreen.data.ContentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): Database {
        return Database.getDatabase(app)
    }
    @Provides
    fun provideUserDao(database: Database): ContentDao {
        return database.contentDao
    }
    @Provides
    fun provideProjectFolderDao(database: Database): com.example.organizeit.showProjects.data.ProjectFolderDao {
        return database.projectFolderDao
    }


}
