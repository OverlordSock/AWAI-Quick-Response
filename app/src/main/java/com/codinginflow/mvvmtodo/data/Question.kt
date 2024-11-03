import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

// Define the Question entity for Room database
@Entity(tableName = "question_table")
@Parcelize
data class Question(
@PrimaryKey val id: Int,
val questionText: String,
val isAnswered: Boolean,
val ageCategory: Boolean,
val isCompleted: Boolean
) : Parcelable





