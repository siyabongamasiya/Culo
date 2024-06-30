package music.project.culo.View

import kotlinx.serialization.Serializable


@Serializable
sealed class Routes{
    @Serializable
    object Homescreen : Routes()

    @Serializable
    data class CurrentSongScreen(
        val currentSong : String = "default"
    ) : Routes()

    @Serializable
    data class Playlist_listScreen(
        val playlistname : String = "default") : Routes()

    @Serializable
    data class PostDetailsScreen(val song : String = "default")

    @Serializable
    data class AudioCuttingScreen(val song : String = "default")

    @Serializable
    data class Top3PostDetailsScreen(val song : String = "default")
}

