package com.codinginflow.mvvmtodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codinginflow.mvvmtodo.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

// Database class for Room, defining entities and version
@Database(entities = [Contact::class], version = 1)
abstract class ContactDatabase : RoomDatabase() {
    // Abstract function to get the ContactDao
    abstract fun contactDao(): ContactDao

    // Callback class to populate the database on creation
    class Callback @Inject constructor(
        private val database: Provider<ContactDatabase>,  // Provider to get an instance of Database
        @ApplicationScope private val applicationScope: CoroutineScope  // CoroutineScope for launching coroutines
    ) : RoomDatabase.Callback() {
        // Override onCreate method to insert initial tasks into the database
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().contactDao()  // Get the ContactDao instance
            applicationScope.launch {
                // Insert sample contact
                dao.insert(Contact("ExampleName", "0800838383",
                    "Caterer", false))
            }
        }
    }
}
