package music.project.culo.Utils

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import music.project.culo.Data.LocalRepositoryImpl.LocalRepoImpl
import music.project.culo.Domain.Model.Playlist

object PlaylistProvider{
    val localRepo = LocalRepoImpl()
    private var _playlists = MutableStateFlow<MutableSet<Playlist>>(mutableSetOf())
    val playlists = _playlists.asStateFlow()

    fun collectPlaylists(context: Context){
        val coroutineScope = CoroutineScope(Dispatchers.IO)

        coroutineScope.launch(Dispatchers.IO) {
            localRepo.getPlaylistsFromRoom(context).collect{playlists ->
                withContext(Dispatchers.Main) {
                    _playlists.value = playlists.toMutableSet()
                    SongManager.setInitialRecents(getRecents(_playlists.value))

                    val containtsRecents = containsRecents(_playlists.value)
                    saveRecents(containtsRecents,context)
                }
            }
        }
    }

    private fun saveRecents(containsRecents : Boolean,context: Context){
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            if (!containsRecents){
                localRepo.savePlaylist(context,Playlist(name = RECENTS_PLAYLIST))
                collectPlaylists(context)
            }
        }

    }

    private fun getRecents(playlists: MutableSet<Playlist>) : Playlist{
        playlists.forEach {playlist ->
            if (playlist.name == RECENTS_PLAYLIST){
                return playlist
            }
        }

        return Playlist(name = RECENTS_PLAYLIST)
    }

    private fun containsRecents(playlists : MutableSet<Playlist>) : Boolean{
        playlists.forEach { playlist ->
            if (playlist.name == RECENTS_PLAYLIST){
                return true
            }
        }

        return false
    }
}