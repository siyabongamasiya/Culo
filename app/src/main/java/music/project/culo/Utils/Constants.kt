package music.project.culo.Utils

import android.os.Environment
import music.project.culo.CuloApp

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

val PREVIOUS = "Previous"
val PLAY = "Play"
val PAUSE = "Pause"
val NEXT = "Next"


val FOREGROUND_SERVICE_REQUEST_CODE = 1
val NOTIFICATION_ID = 1

val audioMMIMETYPE = "audio/*"
val QUEDE_SONG_URL = 0

val DESTINATIONPATH = CuloApp.getContext().filesDir.path + "/Culo"
val typeVideo ="Video"
val typeAudio = "Audio"

val POST_TYPE_OVERLAY = "overlay"
val POST_TYPE_CAPTION = "caption"

val ISSAVED = "is saved"

val RECENTS_PLAYLIST = "recents"

enum class MusicActions{
    start,
    pauseplay,
    next,
    previous,
    seekto,
    shuffle,
    queue
}

enum class ChannelDetails(val string : String){
    CHANNEL_ID("Foreground Notifications"),
    CHANNEL_NAME("Music channel"),
    DESCRIPTION("A Channel for music notification in foreground service")
}

enum class ForegroundIntentExtras{
    ID,
    LIST,
    SEEK
}

enum class States{
    INPROGRESS,
    DONE_SUCCESS,
    DONE_FAILED
}




