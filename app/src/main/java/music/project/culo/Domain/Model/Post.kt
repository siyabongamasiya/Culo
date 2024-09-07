package music.project.culo.Domain.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Posts")
data class Post(
    @ColumnInfo(name = "name")
    val name : String = "",
    @ColumnInfo(name = "type")
    val type : String,
    @PrimaryKey
    @ColumnInfo(name = "url")
    val url : String)
