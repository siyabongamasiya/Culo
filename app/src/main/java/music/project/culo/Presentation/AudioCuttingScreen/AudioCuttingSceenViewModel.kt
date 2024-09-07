package music.project.culo.Presentation.AudioCuttingScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import music.project.culo.CuloApp
import music.project.culo.Data.LocalRepositoryImpl.LocalRepoImpl
import music.project.culo.FFMPEG.FFMPEGprocessor
import music.project.culo.ForegroundService.ForegroundService
import music.project.culo.SongManager.SongManager
import music.project.culo.Utils.EventBus
import music.project.culo.Utils.States
import music.project.culo.Utils.ForegroundIntentExtras
import music.project.culo.Utils.MusicActions
import music.project.culo.Utils.OutofRange

class AudioCuttingSceenViewModel : ViewModel() {
    //UI
    private var _range = MutableStateFlow("Current Range- 00:00 to 00:30 (30secs)")
    val range = _range.asStateFlow()

    //dependencies
    private val ffmpeGprocessor = FFMPEGprocessor()
    private val localRepoImpl = LocalRepoImpl()

    //fields
    private var currentStart = -1L
    private lateinit var imageUrl : String
    private var currentTime : Long = 0


    fun setCurrentTime(currentTimeMs: Long){
        currentTime = currentTimeMs
    }

    fun resetTimeToCurrentTime(){
        SeekTo(currentTime)
    }


    fun SeekTo(seek : Long){
        val context = CuloApp.getContext()
        Intent(context, ForegroundService :: class.java).also { intent ->
            intent.action = MusicActions.seekto.toString()
            intent.putExtra(ForegroundIntentExtras.SEEK.toString(),seek)
            context.startForegroundService(intent)
        }
    }

    fun calculateRange(currentTimeMs : Long,totalTimeMs : Long){
        if ((currentTimeMs + 30000) <= totalTimeMs) {
            val startRange = SongManager.formatCurrentTime(currentTimeMs)
            val endRange = SongManager.formatCurrentTime(currentTimeMs + 30000)

            val string = "Current Range- $startRange to $endRange (30secs)"
            _range.value = string
            //forcing the slider
            SeekTo(currentTimeMs)
            currentStart = currentTimeMs
        }else{
            _range.value = OutofRange
            currentStart = -1L
        }

    }

    fun pauseplayCurrentSong(context: Context){
        Intent(context, ForegroundService :: class.java).also { intent ->
            intent.action = MusicActions.pauseplay.toString()
            context.startForegroundService(intent)
        }
    }

    fun TrimAudio(input: String, context: Context){
        viewModelScope.launch {
            EventBus.updateState(States.INPROGRESS.toString())
            ffmpeGprocessor.OverlayOnImage(currentStart,input,imageUrl).collect{post ->
                withContext(Dispatchers.IO) {
                    localRepoImpl.savePost(context, post)
                }
            }
        }

    }

    fun setImageUrl(url : String){
        imageUrl = url
    }
}