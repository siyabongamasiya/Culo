package music.project.culo.Presentation

import kotlinx.serialization.Serializable


@Serializable
sealed class Routes{
    @Serializable
    object Homescreen 

    @Serializable
    data class CurrentSongScreen(
        val default : String = "default"
    ) : Routes()

    @Serializable
    data class Playlist_listScreen(
        val playlistname : String = "default") : Routes()

    @Serializable
    data class PostDetailsScreen(val artist : String = "default",val title : String)

    @Serializable
    data class AudioCuttingScreen(val uri : String = "",val currentTime : Long)

    @Serializable
    data class PostViewerScreen(val url : String = "")

    @Serializable
    data class Top3PostDetailsScreen(val song : String = "default")
}

