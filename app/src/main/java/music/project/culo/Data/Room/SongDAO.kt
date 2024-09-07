package music.project.culo.Data.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Post
import music.project.culo.Domain.Model.Song

@Dao
interface SongDAO {

    @Query("SELECT * FROM Songs")
    fun getSongs() : Flow<List<Song>>

    @Query("SELECT * FROM Posts")
    fun getPosts() : Flow<List<Post>>

    @Query("SELECT * FROM playlists")
    fun getPlaylists() : Flow<List<Playlist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun SaveSong(songs : Song)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun SavePost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun SavePlaylist(playlist: Playlist)

    @Delete
    fun DeletePost(post: Post)

    @Delete
    fun DeletePlaylists(playlist: Playlist)

    @Update
    fun UpdatePlaylist(playlist: Playlist)

}