package com.codinginflow.mvvmtodo.di

import android.app.Application
import com.codinginflow.mvvmtodo.data.UserContactDatabase



import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

// Dagger module for providing application-level dependencies
@Module
@InstallIn(ApplicationComponent::class)
object AppModuleUserContact {

    // Provides a singleton instance of TaskDatabase
    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,  // Application context
        callback: UserContactDatabase.Callback  // Callback for database operations
    ) = Room.databaseBuilder(app, UserContactDatabase::class.java, "task_database")  // Build the database
        .fallbackToDestructiveMigration()  // Allow destructive migrations
        .addCallback(callback)  // Add the callback
        .build()  // Build the database instance

    // Provides TaskDao from the TaskDatabase
    @Provides
    fun provideTaskDao(db: UserContactDatabase) = db.userContactDao()

    // Provides a CoroutineScope with a SupervisorJob for application-level coroutines
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

// Custom qualifier annotation for ApplicationScope
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

