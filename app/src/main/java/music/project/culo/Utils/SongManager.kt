package music.project.culo.Utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import music.project.culo.Domain.Model.CurrentSongDetails
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Song

object SongManager{
    private var _currentSongDetails = MutableStateFlow(CurrentSongDetails())
    val currentSongDetails = _currentSongDetails.asStateFlow()

    var recentplays = Playlist(name = RECENTS_PLAYLIST)

    fun setInitialRecents(playlist: Playlist){
        recentplays = playlist
    }

    fun UpdateCurrentSong(currentTimeMs : Long,
                          currentSong : Song,
                          currentRepeatMode : Int,
                          isplaying : Boolean,
                          shuffleModeOn : Boolean = false){

        val currenttime = convertSecondsToHMmSs(currentTimeMs)
        val currenttotaltime = convertSecondsToHMmSs(currentSong.duration.toLong())
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

}