package music.project.culo.Domain.Model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    @ColumnInfo(name = "name")
    var name : String = "",
    @Embedded
    val songs : Songs = Songs()
)
