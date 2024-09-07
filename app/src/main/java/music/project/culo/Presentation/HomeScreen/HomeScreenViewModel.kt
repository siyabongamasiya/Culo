package music.project.culo.Presentation.HomeScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import music.project.culo.CuloApp
import music.project.culo.Data.LocalRepositoryImpl.LocalRepoImpl
import music.project.culo.Data.Room.Database
import music.project.culo.Domain.MergeLocalAndRoomSOngs.MergeRoomAndLocal
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Post
import music.project.culo.Domain.Model.Song
import music.project.culo.Domain.Model.Songs
import music.project.culo.ForegroundService.ForegroundService
import music.project.culo.SongManager.SongManager
import music.project.culo.Utils.EventBus
import music.project.culo.Utils.ForegroundIntentExtras
import music.project.culo.Utils.MusicActions
import music.project.culo.Utils.PlaylistProvider
import music.project.culo.Utils.States
import music.project.culo.Utils.audioMMIMETYPE
import java.io.Serializable

class HomeScreenViewModel: ViewModel() {
    private var _songlist = MutableStateFlow<List<Song>>(emptyList())
    val songlist = _songlist.asStateFlow()

    private var  _postlist = MutableStateFlow<MutableSet<Post>>(mutableSetOf())
    val postlist = _postlist.asStateFlow()

    private val localRepoImpl = LocalRepoImpl()


    fun collectPosts(context: Context){

        viewModelScope.launch {
            localRepoImpl.getPostsFromRoom(context).collect{posts ->
                _postlist.value = posts.toMutableSet()
            }
        }
    }

    fun getSongs(context : Context){

        viewModelScope.launch(Dispatchers.IO) {

            MergeRoomAndLocal(context){modifiedList ->
                _songlist.value = modifiedList
            }
        }
    }

    fun pauseplayCurrentSong(context: Context){
        Intent(context,ForegroundService :: class.java).also {intent ->
            intent.action = MusicActions.pauseplay.toString()
            context.startForegroundService(intent)
        }
    }

    fun findIndexOfFirstItem(letter : String) : Int{
        var finalindex  = 0
        val sortedlist = _songlist.value.sortedBy {song ->
            song.title
        }
        run vimba@{
            sortedlist.forEachIndexed(){index, song->
                if (song.title.startsWith(letter)){
                    finalindex = index
                    return@vimba
                }
            }
        }


        return finalindex
    }

    fun deletePost(context: Context,post: Post){
        viewModelScope.launch(Dispatchers.IO) {
            localRepoImpl.deletePost(context,post)
            collectPosts(context)

            withContext(Dispatchers.Main){
                Toast.makeText(context,"Done!!",Toast.LENGTH_SHORT).show()
            }
        }
    }




    fun addPlaylist(playlist: Playlist,context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            localRepoImpl.savePlaylist(context,playlist)
            PlaylistProvider.collectPlaylists(context)
        }
    }

    fun deletePlaylist(context: Context,playlist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
            localRepoImpl.deletePlaylist(context,playlist)
            PlaylistProvider.collectPlaylists(context)
        }
    }

    fun updatePlaylist(context: Context,playlist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
            localRepoImpl.updatePlaylist(context,playlist)
        }
    }

}