package music.project.culo.Utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import music.project.culo.Domain.Model.Song

class ListConverter {

    @TypeConverter
    fun fromStringArrayList(value: List<Song>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringArrayList(value: String): List<Song> {
        return try {
            val type = object : TypeToken<List<Song>>(){}.type
            Gson().fromJson(value,type) //using extension function
        } catch (e: Exception) {
            listOf()
        }
    }
}