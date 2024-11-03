package com.codinginflow.mvvmtodo.di

import android.app.Application
import com.codinginflow.mvvmtodo.data.QuestionsDatabase
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

// Dagger module for providing application-level dependencies
@Module
@InstallIn(SingletonComponent::class)
object AppModuleQuestion {

    // Provides a singleton instance of QuestionsDatabase
    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,  // Application context
        callback: QuestionsDatabase.Callback  // Callback for database operations
    ) = Room.databaseBuilder(app, QuestionsDatabase::class.java, "Questions_database")  // Build the database
        .fallbackToDestructiveMigration()  // Allow destructive migrations
        .addCallback(callback)  // Add the callback
        .build()  // Build the database instance

    // Provides QuestionDao from the QuestionsDatabase
    @Provides
    fun provideQuestionDao(db: QuestionsDatabase) = db.questionDao()

    // Provides a single application-level CoroutineScope with SupervisorJob
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

// Custom qualifier annotation for ApplicationScope
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
