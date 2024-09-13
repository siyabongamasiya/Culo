package music.project.culo.Data.local

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import music.project.culo.Domain.LocalRepository.LocalRepo
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Post
import music.project.culo.Domain.Model.Song

class FakeLocalRepo : LocalRepo {
    private var _songsTable = MutableStateFlow(mutableListOf(Song(url = "url1"),Song(url = "url2")))
    val songsTable = _songsTable.asStateFlow()

    private var _localDevice = MutableStateFlow(mutableListOf(Song(url = "url1"),
        Song(url = "url2"),
        Song(url = "url3")))
    val localdevice = _localDevice.asStateFlow()

    private var _playlistsTable = MutableStateFlow(mutableListOf(Playlist()))
    val playlistsTable = _playlistsTable.asStateFlow()


    override suspend fun getSongsFromDevice(context: Context): Flow<List<Song>> {
        return localdevice
    }

    override suspend fun getSongsFromRoom(context: Context): Flow<List<Song>> {
        return songsTable
    }

    override suspend fun getPostsFromRoom(context: Context): Flow<List<Post>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlaylistsFromRoom(context: Context): Flow<List<Playlist>> {
        return playlistsTable
    }

    override suspend fun saveSong(context: Context, song: Song) {
        _songsTable.value.add(song)
    }

    override suspend fun savePost(context: Context, post: Post) {
        TODO("Not yet implemented")
    }

    override suspend fun savePlaylist(context: Context, playlist: Playlist) {
        _playlistsTable.value.add(playlist)
    }

    override suspend fun deletePost(context: Context, post: Post) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlaylist(context: Context, playlist: Playlist) {
        _playlistsTable.value.remove(playlist)
    }

    override suspend fun updatePlaylist(context: Context, playlist: Playlist) {
        _playlistsTable.value = _playlistsTable.value.map { innerplaylist ->
            if (playlist.name == innerplaylist.name){
                playlist
            }else{
                innerplaylist
            }
        }.toMutableList()
    }
}