package music.project.culo.Domain.LocalRepository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Post
import music.project.culo.Domain.Model.Song

interface LocalRepo {
    suspend fun getSongsFromDevice(context: Context) : Flow<List<Song>>
    suspend fun getSongsFromRoom(context: Context) : Flow<List<Song>>

    suspend fun getPostsFromRoom(context: Context) : Flow<List<Post>>

    suspend fun getPlaylistsFromRoom(context: Context) : Flow<List<Playlist>>

    suspend fun saveSong(context: Context,song: Song)

    suspend fun savePost(context: Context,post: Post)

    suspend fun savePlaylist(context: Context,playlist: Playlist)

    suspend fun deletePost(context: Context,post: Post)

    suspend fun deletePlaylist(context: Context,playlist: Playlist)

    suspend fun updatePlaylist(context: Context,playlist: Playlist)
}