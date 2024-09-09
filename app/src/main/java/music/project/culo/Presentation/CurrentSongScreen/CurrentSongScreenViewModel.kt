package music.project.culo.Presentation.CurrentSongScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import music.project.culo.CuloApp
import music.project.culo.Data.LocalRepositoryImpl.LocalRepoImpl
import music.project.culo.Domain.LocalRepository.LocalRepo
import music.project.culo.Domain.MergeLocalAndRoomSOngs.MergeRoomAndLocal
import music.project.culo.Domain.Model.Song
import music.project.culo.Domain.Model.Songs
import music.project.culo.ForegroundService.ForegroundService
import music.project.culo.Utils.ForegroundIntentExtras
import music.project.culo.Utils.MusicActions
import music.project.culo.Utils.audioMMIMETYPE
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class CurrentSongScreenViewModel @Inject constructor(): ViewModel() {
    private lateinit var songlist : List<Song>

    fun getSongs(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            MergeRoomAndLocal(context){modifiedList ->
                songlist = modifiedList
            }
        }
    }

    fun SeekTo(seek : Long,context: Context){
        Intent(context, ForegroundService :: class.java).also { intent ->
            intent.action = MusicActions.seekto.toString()
            intent.putExtra(ForegroundIntentExtras.SEEK.toString(),seek)
            context.startForegroundService(intent)
        }
    }

    fun previousSong(context: Context){
        Intent(context, ForegroundService :: class.java).also { intent ->
            intent.action = MusicActions.previous.toString()
            context.startForegroundService(intent)
        }
    }

    fun pauseplayCurrentSong(context: Context){
        Intent(context, ForegroundService :: class.java).also { intent ->
            intent.action = MusicActions.pauseplay.toString()
            context.startForegroundService(intent)
        }
    }

    fun nextSong(context: Context){
        Intent(context, ForegroundService :: class.java).also { intent ->
            intent.action = MusicActions.next.toString()
            context.startForegroundService(intent)
        }
    }

    fun shuffle(context: Context){
        Intent(context, ForegroundService :: class.java).also { intent ->
            intent.action = MusicActions.shuffle.toString()
            context.startForegroundService(intent)
        }
    }
}