package com.codinginflow.mvvmtodo.data

import UserContact
import UserContactDao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codinginflow.mvvmtodo.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [UserContact::class], version = 1)
abstract class UserContactDatabase : RoomDatabase() {
    abstract fun userContactDao(): UserContactDao

    class Callback @Inject constructor(
        private val database: Provider<UserContactDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().userContactDao()
            applicationScope.launch {
                dao.insertUserContact(UserContact(1, "John Doe", "1234567890", "282982929", "63727363723", "32728373882", "373828373"))
                dao.insertUserContact(UserContact(2, "Jane Smith", "0987654321", "92083920", "283839292", "3627281829", "7328292889"))
            }
        }
    }
}
