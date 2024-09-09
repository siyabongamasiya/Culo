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