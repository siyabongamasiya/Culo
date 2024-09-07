package music.project.culo.Domain.Model

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class Songs(
    var songList: List<Song> = emptyList()
) : Parcelable
