package music.project.culo.SongManager

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import music.project.culo.CuloApp
import music.project.culo.Domain.LocalRepository.LocalRepo
import music.project.culo.Domain.MergeLocalAndRoomSOngs.GetSongs
import music.project.culo.Domain.Model.Song
import music.project.culo.Domain.Model.Songs
import music.project.culo.Utils.SongManager
import music.project.culo.Utils.getPlayMode
import music.project.culo.Utils.savedPlayMode
import javax.inject.Inject

class SongManager @Inject constructor(val exoPlayer: ExoPlayer,
                                      val localrepo: LocalRepo,
                                      val getSongs: GetSongs,
                                      val context: CuloApp) {

    var songList: List<Song> = emptyList()
    var queue : MutableList<Song> = mutableListOf()
    var showNotification : (song : Song) -> Unit = {}
    private var currentSong = Song()

    init {
        getSongs()
    }



    fun getCurrentSong() : Song{
        return currentSong
    }
    fun setNotifier(notifier : (song : Song) -> Unit){
        this.showNotification = notifier
    }

    fun getSongs(){
        CoroutineScope(Dispatchers.IO).launch {
            getSongs.invoke(context){updatedsongs ->
                songList = updatedsongs
            }
        }
    }



    fun startSong(url : String) : Song{
        //create media items
        val mediaitems = songList.let { list ->
            val mediaItems : MutableList<MediaItem> = mutableListOf()
            list.forEach { song ->
                mediaItems.add(MediaItem.fromUri(song.url))
            }

            mediaItems
        }

        //search for index of initial song
        var initialPosition = 0
        songList.forEachIndexed {index,song ->
            if (song.url == url){
                initialPosition = index
            }
        }

        //create listener for playback state
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

        //set exoplayer configuration
        with(exoPlayer){
            setMediaItems(mediaitems)
            addListener(listener)
            seekToDefaultPosition(initialPosition)
            val rMode = getPlayMode(context)
            if(rMode != -1){
                repeatMode = rMode
                exoPlayer.shuffleModeEnabled = false
            }else{
                exoPlayer.shuffleModeEnabled = true
            }
            prepare()
            play()
        }

        //start song,increase number of plays and update UI
        val startsong = songList[initialPosition]
        currentSong = startsong
        increasePlays(currentSong)
        SongManager.UpdateCurrentSong(exoPlayer.currentPosition,startsong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)
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
        //put it on top in recent plays
        topIt(song)


        val savableplaylist = SongManager.recentplays

        song.plays++
        coroutineScope.launch {
            //save song with increased plays
            localrepo.saveSong(context,song)
            //save new adjusted recents playlist
            if (SongManager.recentplays.songs.songList.size == 1) {
                localrepo.savePlaylist(context, savableplaylist)
            }else{
                localrepo.updatePlaylist(context, savableplaylist)
            }

            getSongs()
        }
    }

    private fun topIt(song: Song){
        val index = isInRecents(song)
        if (index >= 0){
            val mutablelist = SongManager.recentplays.songs.songList.toMutableList()
            val removedsong = mutablelist.removeAt(index)
            val newlist : MutableList<Song> = mutableListOf()
            newlist.addAll(mutablelist)
            newlist.add(removedsong)

            SongManager.recentplays.songs.songList = newlist
        }else{
            val mutablelist = SongManager.recentplays.songs.songList.toMutableList()
            val newlist : MutableList<Song> = mutableListOf()
            newlist.addAll(mutablelist)
            newlist.add(song)

            SongManager.recentplays.songs.songList = newlist
        }
    }

    private fun isInRecents(searchedSong : Song) : Int{
        SongManager.recentplays.songs.songList.forEachIndexed{index,song ->
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
        SongManager.UpdateCurrentSong(exoPlayer.currentPosition,currentSong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)

        this.currentSong = currentSong
        return currentSong
    }

    fun SeekTo(target : Long) : Song{
        exoPlayer.seekTo(target)
        val latestSong = songList[exoPlayer.currentMediaItemIndex]
        SongManager.UpdateCurrentSong(target,latestSong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)
        this.currentSong = latestSong
        return songList[exoPlayer.currentMediaItemIndex]
    }

    fun broadcastLatestCurrentTime(){
        val latestSong = songList[exoPlayer.currentMediaItemIndex]
        this.currentSong = latestSong
        SongManager.UpdateCurrentSong(exoPlayer.currentPosition,latestSong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)
    }

    fun playNextSong() : Song{
        exoPlayer.seekToNext()
        val latestSong = songList[exoPlayer.currentMediaItemIndex]
        SongManager.UpdateCurrentSong(0L,latestSong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)
        this.currentSong = latestSong
        return latestSong
    }

    fun playPreiousSong() : Song{
        exoPlayer.seekToPrevious()
        val latestSong = songList[exoPlayer.currentMediaItemIndex]
        SongManager.UpdateCurrentSong(0L,latestSong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)
        this.currentSong = latestSong
        return latestSong
    }

    fun shuffle(){
        if (exoPlayer.repeatMode == ExoPlayer.REPEAT_MODE_ONE && !exoPlayer.shuffleModeEnabled){
            exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
            exoPlayer.shuffleModeEnabled = false
            savedPlayMode(ExoPlayer.REPEAT_MODE_ALL,context)
        }else if (exoPlayer.repeatMode == ExoPlayer.REPEAT_MODE_ALL && !exoPlayer.shuffleModeEnabled){
            exoPlayer.shuffleModeEnabled = true
            savedPlayMode(-1,context)
        }else if (exoPlayer.shuffleModeEnabled){
            exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE
            savedPlayMode(ExoPlayer.REPEAT_MODE_ONE,context)
            exoPlayer.shuffleModeEnabled = false
        }

        val currentSong = songList[exoPlayer.currentMediaItemIndex]
        this.currentSong = currentSong
        SongManager.UpdateCurrentSong(exoPlayer.currentPosition,currentSong,exoPlayer.repeatMode,exoPlayer.isPlaying,exoPlayer.shuffleModeEnabled)
    }

    fun getSong(index : Int): Song {
        return songList[index]
    }

    fun setInitialSongs(songs: Songs){
        if (songList.isEmpty()) {
            songList = songs.songList
        }
    }

    fun isPlaying() : Boolean{
        return  exoPlayer.isPlaying ||
                exoPlayer.isLoading ||
                (exoPlayer.playbackState == ExoPlayer.STATE_BUFFERING)
    }
}