package music.project.culo.Domain.Model

data class CurrentSongDetails(
    val currentTime : String = "",
    val currentTotalTime : String = "",
    val currentTimeMs : Long = 1L,
    val currentTotalTimeMs : Long = 1L,
    val isPlaying : Boolean = false,
    val currentSong: Song = Song(),
    val currentRepeatMode : Int = 0,
    val hasStarted : Boolean = false,
    val shuffleModeOn : Boolean = false
)
