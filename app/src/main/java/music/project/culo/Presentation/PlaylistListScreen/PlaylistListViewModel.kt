package music.project.culo.Presentation.PlaylistListScreen

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import music.project.culo.Domain.LocalRepository.LocalRepo
import music.project.culo.Domain.MergeLocalAndRoomSOngs.GetSongs
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Song
import music.project.culo.ForegroundService.ForegroundService
import music.project.culo.Presentation.Routes
import music.project.culo.Utils.MusicActions
import music.project.culo.Utils.PlaylistProvider
import music.project.culo.Utils.StartSong
import music.project.culo.Utils.findPlaylistbyname
import javax.inject.Inject

@HiltViewModel
class PlaylistListViewModel @Inject constructor(private val localRepo: LocalRepo,val getSongs: GetSongs): ViewModel() {
    private var _currentPlaylist = MutableStateFlow(Playlist(name = ""))
    val currentPlaylist = _currentPlaylist.asStateFlow()

    private var _songlist = MutableStateFlow(listOf(Song()))
    val songlist = _songlist.asStateFlow()
    fun pauseplayCurrentSong(context: Context){
        Intent(context, ForegroundService :: class.java).also { intent ->
            intent.action = MusicActions.pauseplay.toString()
            context.startForegroundService(intent)
        }
    }

    fun getSongs(context : Context){

        viewModelScope.launch(Dispatchers.IO) {
            getSongs.invoke(context){modifiedList ->
                val sortedlist = modifiedList.sortedBy {song ->
                    song.artist
                }
                _songlist.value = sortedlist.filter {song ->
                    song.liked
                }
            }
        }
    }


    fun findIndexOfFirstItem(letter : String) : Int{
        var finalindex : Int = 0
//        _songlist.value.forEachIndexed(){index, song->
//            if (song.artist.startsWith(letter)){
//                finalindex = index
//                return@forEachIndexed
//            }
//        }

        return finalindex
    }

    fun collectPlaylists(name: String){
        viewModelScope.launch(Dispatchers.IO) {
            PlaylistProvider.playlists.collect{playlists ->
                _currentPlaylist.value = findPlaylistbyname(playlists.toList(),name)
            }
        }
    }

    fun updatePlaylistName(context: Context,newName : String){
        if (newName != null) {
            _currentPlaylist.value.name = newName
        }

        updatePlaylist(context,_currentPlaylist.value)
    }

    fun updatePlaylist(context: Context,playlist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
            localRepo.updatePlaylist(context,playlist)
        }
    }

    fun startSong(isSameSong : Boolean,
                  navController: NavHostController,
                  song: Song, context: Context){
        if (isSameSong){
            navController.navigate(Routes.CurrentSongScreen())
        }else{
            navController.navigate(Routes.CurrentSongScreen())
            StartSong(song,context,songlist.value)
        }
    }
}