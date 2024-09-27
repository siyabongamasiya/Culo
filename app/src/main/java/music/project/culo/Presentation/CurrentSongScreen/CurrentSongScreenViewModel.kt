package music.project.culo.Presentation.CurrentSongScreen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import music.project.culo.Domain.LocalRepository.LocalRepo
import music.project.culo.Domain.MergeLocalAndRoomSOngs.MergeRoomAndLocal
import music.project.culo.Domain.Model.Song
import music.project.culo.ForegroundService.ForegroundService
import music.project.culo.Utils.ERROR
import music.project.culo.Utils.ForegroundIntentExtras
import music.project.culo.Utils.MusicActions
import javax.inject.Inject

@HiltViewModel
class CurrentSongScreenViewModel @Inject constructor(val localRepo: LocalRepo): ViewModel() {
    private lateinit var songlist : List<Song>

    fun getSongs(context: Context){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                MergeRoomAndLocal(context, localRepo) { modifiedList ->
                    songlist = modifiedList
                }
            }
        }catch(e : Exception){
            Toast.makeText(context, ERROR, Toast.LENGTH_SHORT).show()
        }
    }

    fun SeekTo(seek : Long,context: Context){
        try {
            Intent(context, ForegroundService::class.java).also { intent ->
                intent.action = MusicActions.seekto.toString()
                intent.putExtra(ForegroundIntentExtras.SEEK.toString(), seek)
                context.startForegroundService(intent)
            }
        }catch(e : Exception){
            Toast.makeText(context,ERROR,Toast.LENGTH_SHORT).show()
        }
    }

    fun previousSong(context: Context){
        try {
            Intent(context, ForegroundService::class.java).also { intent ->
                intent.action = MusicActions.previous.toString()
                context.startForegroundService(intent)
            }
        }catch(e : Exception){
            Toast.makeText(context,ERROR,Toast.LENGTH_SHORT).show()
        }
    }

    fun pauseplayCurrentSong(context: Context){
        try {
            Intent(context, ForegroundService::class.java).also { intent ->
                intent.action = MusicActions.pauseplay.toString()
                context.startForegroundService(intent)
            }
        }catch(e : Exception){
            Toast.makeText(context,ERROR,Toast.LENGTH_SHORT).show()
        }
    }

    fun nextSong(context: Context){
        try {
            Intent(context, ForegroundService::class.java).also { intent ->
                intent.action = MusicActions.next.toString()
                context.startForegroundService(intent)
            }
        }catch(e : Exception){
            Toast.makeText(context,ERROR,Toast.LENGTH_SHORT).show()
        }
    }

    fun shuffle(context: Context){
        try {
            Intent(context, ForegroundService::class.java).also { intent ->
                intent.action = MusicActions.shuffle.toString()
                context.startForegroundService(intent)
            }
        }catch(e : Exception){
            Toast.makeText(context,ERROR,Toast.LENGTH_SHORT).show()
        }
    }
}