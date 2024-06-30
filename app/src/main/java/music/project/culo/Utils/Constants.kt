package music.project.culo.Utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Brush

enum class SongOptions(val option : String){
    Add_to_Playlist("Add to Playlist"),
    Add_to_Liked("Add to Liked"),
    Remove_from_Liked("Remove from liked"),
    Add_to_Queue("Add to Queue"),
    Share("Share"),
    Delete("Delete")
}

enum class TestTags(val tag : String){
    Bottom_Bar("bottom bar"),
    Playlists("playlists"),
    Horizontal_Pager("horizontal pager"),
    CurrentSongScreen("current song"),
    RecentlyPlayed("recently played"),
    AllSongsSearchBar("all songs Search bar"),
    CreatePlaylistButton("create playlist button"),
    CurrentSongSlider("current song slider"),
    CurrentSongTime("current song time"),
    CreatePost("create post"),
    Playlist("playlist"),
    Kebab("playlist"),
}

enum class operatorOptions(val option : String){
    Add_to_Playlist("Add to Playlist"),
    Share("Share"),
    Create_post("Create Post"),
    Overlay_On_Image("Overlay on Image"),
}

//val kebabiconSize = 50
val iconSize = 30
val songOperatorsize = 80
val playlistsurfaceSize = 120

val Instruction = "*Select 30 seconds Range for audio below*"
val OutofRange = "Out of Song Range!!"
val NormalOption = "normal options"
val PlaylistOptions = "playlist options"


