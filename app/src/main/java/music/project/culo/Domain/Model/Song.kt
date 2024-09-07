package music.project.culo.Domain.Model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import music.project.culo.Data.Room.SongDAO

@Parcelize
@Entity(tableName = "Songs")
data class Song(
    @PrimaryKey(autoGenerate = true)
    val uid : Int = 0,
    @ColumnInfo(name = "url")
    val url : String ="",
    @ColumnInfo(name = "title")
    val title : String = "",
    @ColumnInfo(name = "artist")
    val artist : String = "",
    @ColumnInfo(name = "duration")
    val duration : String = "",
    @ColumnInfo(name ="id")
    val id : String = "",
    @ColumnInfo(name = "plays")
    var plays : Int = 0,
    @ColumnInfo(name = "liked")
    var liked : Boolean = false) : Parcelable{

    override fun equals(other: Any?): Boolean {
        val othersong = other as Song

        return othersong.url == this.url
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + plays
        result = 31 * result + liked.hashCode()
        return result
    }

    fun getSearchText() : String{
        return "$title $artist"
    }
}