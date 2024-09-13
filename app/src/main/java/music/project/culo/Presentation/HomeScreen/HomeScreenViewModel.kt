package music.project.culo.Presentation.HomeScreen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import music.project.culo.Domain.LocalRepository.LocalRepo
import music.project.culo.Domain.MergeLocalAndRoomSOngs.MergeRoomAndLocal
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Post
import music.project.culo.Domain.Model.Song
import music.project.culo.ForegroundService.ForegroundService
import music.project.culo.Utils.MusicActions
import music.project.culo.Utils.PlaylistProvider
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val localRepoImpl: LocalRepo): ViewModel() {
    private var _songlist = MutableStateFlow<List<Song>>(emptyList())
    val songlist = _songlist.asStateFlow()

    private var  _postlist = MutableStateFlow<MutableSet<Post>>(mutableSetOf())
    val postlist = _postlist.asStateFlow()




    fun collectPosts(context: Context){

        viewModelScope.launch {
            localRepoImpl.getPostsFromRoom(context).collect{posts ->
                _postlist.value = posts.toMutableSet()
            }
        }
    }

    fun getSongs(context : Context){
        viewModelScope.launch(Dispatchers.IO) {

            MergeRoomAndLocal(context,localRepoImpl){modifiedList ->
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