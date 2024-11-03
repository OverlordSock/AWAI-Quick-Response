import androidx.room.*

@Dao
interface QuestionDao {
    @Insert
    fun insertQuestion(question: Question)
}


