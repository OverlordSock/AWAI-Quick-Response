package com.codinginflow.mvvmtodo.di

import android.app.Application
import androidx.room.Room
import com.codinginflow.mvvmtodo.data.UserProfileDatabase
import com.codinginflow.mvvmtodo.data.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides // Using @Provides because we do not own the class or it needs configurations
    @Singleton
    fun provideProfileDatabase( // For Profile Database
        app: Application,
        callback: UserProfileDatabase.Callback
    ) = Room.databaseBuilder(app, UserProfileDatabase::class.java, "profile_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides // Using @Provides because we do not own the class or it needs configurations
    @Singleton
    fun provideContactDatabase( // For Contact Database
        app: Application,
        callback: ContactDatabase.Callback
    ) = Room.databaseBuilder(app, ContactDatabase::class.java, "contact_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides // For Profile Database
    fun provideUserProfileDao(db: UserProfileDatabase) = db.userProfileDao()

    @Provides // For Contact Database
    fun provideContactDao(db: ContactDatabase) = db.contactDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

