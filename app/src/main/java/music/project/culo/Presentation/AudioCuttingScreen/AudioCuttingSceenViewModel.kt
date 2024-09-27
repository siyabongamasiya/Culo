package music.project.culo.Presentation.AudioCuttingScreen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import music.project.culo.Domain.LocalRepository.LocalRepo
import music.project.culo.FFMPEG.FFMPEGprocessor
import music.project.culo.ForegroundService.ForegroundService
import music.project.culo.Utils.ERROR
import music.project.culo.Utils.SongManager
import music.project.culo.Utils.EventBus
import music.project.culo.Utils.States
import music.project.culo.Utils.ForegroundIntentExtras
import music.project.culo.Utils.MusicActions
import music.project.culo.Utils.OutofRange
import music.project.culo.Utils.convertSecondsToHMmSs
import javax.inject.Inject

@HiltViewModel
class AudioCuttingSceenViewModel @Inject constructor(private val localRepoImpl : LocalRepo,
                                 private val ffmpeGprocessor : FFMPEGprocessor) : ViewModel() {
    //UI
    private var _range = MutableStateFlow("Current Range- 00:00 to 00:30 (30secs)")
    val range = _range.asStateFlow()

    //fields
    private var currentStart = -1L
    private lateinit var imageUrl : String
    private var currentTime : Long = 0


    fun setCurrentTime(currentTimeMs: Long){
        currentTime = currentTimeMs
    }

    fun resetTimeToCurrentTime(context: Context){
        SeekTo(currentTime,context)
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

    fun calculateRange(currentTimeMs : Long,totalTimeMs : Long,context: Context){
        try {
            if ((currentTimeMs + 30000) <= totalTimeMs) {
                val startRange = convertSecondsToHMmSs(currentTimeMs)
                val endRange = convertSecondsToHMmSs(currentTimeMs + 30000)

                val string = "Current Range- $startRange to $endRange (30secs)"
                _range.value = string
                //forcing the slider
                SeekTo(currentTimeMs, context)
                currentStart = currentTimeMs
            } else {
                _range.value = OutofRange
                currentStart = -1L
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

    fun TrimAudio(input: String, context: Context){
        try {
            viewModelScope.launch {
                EventBus.updateState(States.INPROGRESS.toString())
//            ffmpeGprocessor.OverlayOnImage(currentStart,input,imageUrl).collect{post ->
//                withContext(Dispatchers.IO) {
//                    localRepoImpl.savePost(context, post)
//                }
//            }
            }
        }catch(e : Exception){
            Toast.makeText(context,ERROR,Toast.LENGTH_SHORT).show()
        }

    }

    fun setImageUrl(url : String){
        imageUrl = url
    }
}