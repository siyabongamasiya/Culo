package music.project.culo.SongManager

import android.content.Context
import android.content.Intent
import android.media.metrics.PlaybackStateEvent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import music.project.culo.CuloApp
import music.project.culo.Data.LocalRepositoryImpl.LocalRepoImpl
import music.project.culo.Domain.Model.CurrentSongDetails
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Song
import music.project.culo.Domain.Model.Songs
import music.project.culo.ForegroundService.ForegroundService
import music.project.culo.Utils.MusicActions
import music.project.culo.Utils.PlaylistProvider
import music.project.culo.Utils.RECENTS_PLAYLIST
import music.project.culo.Utils.getPlayMode
import music.project.culo.Utils.savedPlayMode

object SongManager {
    lateinit var exoPlayer : ExoPlayer
    lateinit var songList: List<Song>
    var queue : MutableList<Song> = mutableListOf()
    var showNotification : (song : Song) -> Unit = {}
    private var currentSong = Song()
    private var localrepo = LocalRepoImpl()


    private var _currentSongDetails = MutableStateFlow(CurrentSongDetails())
    val currentSongDetails = _currentSongDetails.asStateFlow()

    private var recentplays = Playlist(name = RECENTS_PLAYLIST)



    fun createExoplayer(context: Context){
       exoPlayer = ExoPlayer.Builder(context).build()
    }

    fun getCurrentSong() : Song{
        return currentSong
    }
    fun setNotifier(notifier : (song : Song) -> Unit){
        this.showNotification = notifier
    }

    fun setInitialRecents(playlist: Playlist){
        recentplays = playlist
    }
    fun startSong(url : String) : Song{
        val mediaitems = songList.let { list ->
            val mediaItems : MutableList<MediaItem> = mutableListOf()
            list.forEach { song ->
                mediaItems.add(MediaItem.fromUri(song.url))
            }

            mediaItems
        }

        var initialPosition = 0
        songList.forEachIndexed {index,song ->
            if (song.url == url){
                initialPosition = index
            }
        }

        val listener = object : Player.Listener{

            override fun onPlaybackStateChanged(playbackState: Int) {

                if (playbackState == Player.STATE_ENDED){
                    if (queue.size > 0){
                        val song = queue[0]
                        val positionOnMainList = songList.indexOf(song)
                        queue.removeAt(0)

                        exoPlayer.seekTo(positionOnMainList,0L)

                    }
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {


                if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO){
                    if (queue.size > 0){
                        val song = queue[0]
                        val positionOnMainList = songList.indexOf(song)
                        queue.removeAt(0)

                        exoPlayer.seekTo(positionOnMainList,0L)
                    }
                    increasePlays(currentSong)
                }
                val currentSong = songList[exoPlayer.currentMediaItemIndex]

                showNotification.invoke(currentSong)
            }
        }

        with(exoPlayer){
            setMediaItems(mediaitems)
            addListener(listener)
            seekToDefaultPosition(initialPosition)
            val rMode = getPlayMode(CuloApp.getContext())
            if(rMode != -1){
                repeatMode = rMode
                exoPlayer.shuffleModeEnabled = false
            }else{
                exoPlayer.shuffleModeEnabled = true
            }
            prepare()
            play()
        }

        val startsong = songList[initialPosition]
        currentSong = startsong
        increasePlays(currentSong)
        UpdateCurrentSong(exoPlayer.currentPosition,startsong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)
        return startsong
    }

    fun queue(url : String){
        songList.forEach { song ->
            if (song.url == url){
                queue.add(song)
            }
        }
    }

    private fun increasePlays(song: Song){
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        topIt(song)

        song.plays++
        coroutineScope.launch {
            localrepo.saveSong(CuloApp.getContext(),song)
            localrepo.updatePlaylist(CuloApp.getContext(),recentplays)
        }
    }

    private fun topIt(song: Song){
        val index = isInRecents(song)
        if (index >= 0){
            val mutablelist = recentplays.songs.songList.toMutableList()
            val removedsong = mutablelist.removeAt(index)
            val newlist : MutableList<Song> = mutableListOf()
            newlist.addAll(mutablelist)
            newlist.add(removedsong)

            recentplays.songs.songList = newlist
        }else{
            val mutablelist = recentplays.songs.songList.toMutableList()
            val newlist : MutableList<Song> = mutableListOf()
            newlist.addAll(mutablelist)
            newlist.add(song)

            recentplays.songs.songList = newlist
        }


    }

    private fun isInRecents(searchedSong : Song) : Int{
        recentplays.songs.songList.forEachIndexed{index,song ->
            if (song.url == searchedSong.url){
                return index
            }
        }

        return -1
    }

    fun playOrPause() : Song{
        if (exoPlayer.isPlaying){
            exoPlayer.pause()
        }else{
            exoPlayer.play()
        }

        val currentSong = songList[exoPlayer.currentMediaItemIndex]
        UpdateCurrentSong(exoPlayer.currentPosition,currentSong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)

        this.currentSong = currentSong
        return currentSong
    }

    fun SeekTo(target : Long) : Song{
        exoPlayer.seekTo(target)
        val latestSong = songList[exoPlayer.currentMediaItemIndex]
        UpdateCurrentSong(target,latestSong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)
        this.currentSong = latestSong
        return songList[exoPlayer.currentMediaItemIndex]
    }

    fun broadcastLatestCurrentTime(){
        val latestSong = songList[exoPlayer.currentMediaItemIndex]
        this.currentSong = latestSong
        UpdateCurrentSong(exoPlayer.currentPosition,latestSong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)
    }

    fun playNextSong() : Song{
        exoPlayer.seekToNext()
        val latestSong = songList[exoPlayer.currentMediaItemIndex]
        UpdateCurrentSong(0L,latestSong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)
        this.currentSong = latestSong
        return latestSong
    }

    fun playPreiousSong() : Song{
        exoPlayer.seekToPrevious()
        val latestSong = songList[exoPlayer.currentMediaItemIndex]
        UpdateCurrentSong(0L,latestSong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)
        this.currentSong = latestSong
        return latestSong
    }

    fun shuffle(){
        if (exoPlayer.repeatMode == ExoPlayer.REPEAT_MODE_ONE && !exoPlayer.shuffleModeEnabled){
            exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
            exoPlayer.shuffleModeEnabled = false
            savedPlayMode(ExoPlayer.REPEAT_MODE_ALL,CuloApp.getContext())
        }else if (exoPlayer.repeatMode == ExoPlayer.REPEAT_MODE_ALL && !exoPlayer.shuffleModeEnabled){
            exoPlayer.shuffleModeEnabled = true
            savedPlayMode(-1,CuloApp.getContext())
        }else if (exoPlayer.shuffleModeEnabled){
            exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE
            savedPlayMode(ExoPlayer.REPEAT_MODE_ONE,CuloApp.getContext())
            exoPlayer.shuffleModeEnabled = false
        }

        val currentSong = songList[exoPlayer.currentMediaItemIndex]
        this.currentSong = currentSong
        UpdateCurrentSong(exoPlayer.currentPosition,currentSong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)
    }

    fun getSong(index : Int): Song {
        return songList[index]
    }

    fun setSongs(songs: Songs){
        songList = songs.songList
    }

    fun isPlaying() : Boolean{
        return  exoPlayer.isPlaying ||
                exoPlayer.isLoading ||
                (exoPlayer.playbackState == ExoPlayer.STATE_BUFFERING)
    }

    fun UpdateCurrentSong(currentTimeMs : Long,
                          currentSong : Song,
                          currentRepeatMode : Int,
                          isplaying : Boolean,
                          shuffleModeOn : Boolean = false){

        val currenttime = formatCurrentTime(currentTimeMs)
        val currenttotaltime = formatTotalTime(currentSong)
        val currentrepeatmode = currentRepeatMode

        _currentSongDetails.value = CurrentSongDetails(
            currenttime,
            currenttotaltime,
            currentTimeMs,
            currentSong.duration.toLong(),
            isplaying,
            currentSong,
            currentrepeatmode,
            true,
            shuffleModeOn)
    }

    fun formatCurrentTime(currentTime: Long) : String{
        //calculate current time
        val Ctotalsecs = currentTime/1000
        val CMinutes = Ctotalsecs/60
        val Csecs = Ctotalsecs%60

        //formating
        var Cminutesformatted = ""
        var Csecsformatted  = ""

        //adding zeros if less than 9
        if (CMinutes <= 9){
            Cminutesformatted = "0${CMinutes}"
        }else{
            Cminutesformatted = "${CMinutes}"
        }

        if (Csecs <= 9){
            Csecsformatted = "0${Csecs}"
        }else{
            Csecsformatted = "${Csecs}"
        }

        return "$Cminutesformatted : $Csecsformatted"
    }

    private fun formatTotalTime(song: Song) : String{
        //calculate total time
        val Ttotalsecs = song.duration.toLong()/1000
        val TMinutes = Ttotalsecs/60
        val Tsecs = Ttotalsecs%60

        //formating
        var Tminutesformatted = ""
        var Tsecsformatted  = ""

        //adding zeros if less than 9
        if (TMinutes <= 9){
            Tminutesformatted = "0${TMinutes}"
        }else{
            Tminutesformatted = "${TMinutes}"
        }

        if (Tsecs <= 9){
            Tsecsformatted = "0${Tsecs}"
        }else{
            Tsecsformatted = "${Tsecs}"
        }

        return "$Tminutesformatted : $Tsecsformatted"
    }
}