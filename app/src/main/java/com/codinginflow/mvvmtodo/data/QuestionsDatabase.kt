package com.codinginflow.mvvmtodo.data

import Question
import QuestionDao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codinginflow.mvvmtodo.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

// Database class for Room, defining entities and version
@Database(entities = [Question::class], version = 1)
abstract class QuestionsDatabase : RoomDatabase() {
    // Abstract function to get the QuestionDao
    abstract fun questionDao(): QuestionDao

    // Callback class to populate the database on creation
    class Callback @Inject constructor(
        private val database: Provider<QuestionsDatabase>,  // Provider to get an instance of QuestionsDatabase
        @ApplicationScope private val applicationScope: CoroutineScope  // CoroutineScope for launching coroutines
    ) : RoomDatabase.Callback() {
        // Override onCreate method to insert initial tasks into the database
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().questionDao()  // Get the QuestionDao instance
            applicationScope.launch {
                // Insert sample questions into the database
                dao.insertQuestion(Question(1, "Example question 1", false, true, false))
                dao.insertQuestion(Question(2, "Example question 2", false, true, false))
            }
        }
    }
}
