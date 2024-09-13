package music.project.culo.Data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Post
import music.project.culo.Domain.Model.Song
import music.project.culo.Utils.ListConverter

@androidx.room.Database(entities = [Song::class,Playlist::class,Post::class], exportSchema = true, version = 1)
@TypeConverters(ListConverter::class)
abstract class SongDatabase : RoomDatabase(){
    abstract fun getDAO() : SongDAO

    companion object{
        var SongDATABASE_INSTANCE : SongDatabase? = null

        fun getDatabaseInsatance(context: Context) : SongDatabase{
            if(SongDATABASE_INSTANCE == null) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "Songs Database"
                ).fallbackToDestructiveMigration()
                    .build()
                SongDATABASE_INSTANCE = instance
                // return instance
                return instance
            }else{
               return SongDATABASE_INSTANCE as SongDatabase
            }
        }
    }
}