package music.project.culo.Data.Room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Post
import music.project.culo.Domain.Model.Song
import music.project.culo.Domain.Model.Songs
import music.project.culo.Utils.ListConverter

@androidx.room.Database(entities = [Song::class,Playlist::class,Post::class], exportSchema = true, version = 1)
@TypeConverters(ListConverter::class)
abstract class Database : RoomDatabase(){
    abstract fun getDAO() : SongDAO

    companion object{
        var DATABASE_INSTANCE : Database? = null

        fun getDatabaseInsatance(context: Context) : Database{
            if(DATABASE_INSTANCE == null) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "Songs Database"
                ).fallbackToDestructiveMigration()
                    .build()
                DATABASE_INSTANCE = instance
                // return instance
                return instance
            }else{
               return DATABASE_INSTANCE as Database
            }
        }
    }
}