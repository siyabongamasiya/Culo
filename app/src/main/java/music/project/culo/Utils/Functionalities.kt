package music.project.culo.Utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import music.project.culo.Data.local.LocalRepoImpl
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Song
import music.project.culo.Domain.Model.Songs
import music.project.culo.ForegroundService.ForegroundService
import music.project.culo.Presentation.Routes
import java.io.File
import java.util.concurrent.TimeUnit



suspend fun ContentResolver.getSongsFromMediaStorage(context: Context) : Flow<List<Song>> {
    val contentResolver = this
    return flow{
        var listoflocalsongs = mutableListOf<Song>()

        //create projection of columns
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.ALBUM_ID)
        //create query using contentresolver
        val query = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null
        )
        //receive data from query and update _audios
        query?.use { cursor ->


            while (cursor.moveToNext()){
                val urlIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)
                val artistIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)
                val titleIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)
                val durationIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)
                val IdIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID)

                val url = cursor.getString(urlIndex)
                val artist = cursor.getString(artistIndex)
                val title = cursor.getString(titleIndex)
                val duration = cursor.getString(durationIndex)
                val albumID = cursor.getString(IdIndex)

                val audio = Song(artist = artist, duration = duration, id = albumID, title = title, url = url)

                listoflocalsongs.add(audio)
            }
        }


        //add room details "plays and liked" and return list
        emit(listoflocalsongs)
    }
}

fun getArt(id : String) : Uri? {
    val uriContent = Uri.parse("content://media/external/audio/albumart")
    try {
        val imageUrl = ContentUris.withAppendedId(uriContent,id.toLong())
        return imageUrl
    }catch (exception : Exception){

    }
    return null
}

//fun OverlayImage(
//    start: Long,
//    audioinput: String,
//    imageUrl: String,
//    context: Context
//) = callbackFlow{
//
//    //getImagePath(imageUrl)
//    val audiooutput = generateFilename(typeAudio)
//    val coroutine = CoroutineScope(Dispatchers.Default)
//
//    val audiocommand = arrayOf("-y","-i",
//        audioinput,
//        "-ss",
//        convertSecondsToHMmSs(start),
//        "-to",
//        convertSecondsToHMmSs(start+30000),
//        audiooutput)
//
//    executeWithArgumentsAsync(audiocommand){session ->
//        if(ReturnCode.isSuccess(session.returnCode)){
//                val outputpath = generateFilename(typeVideo)
////            cmd.add("-y");
////            cmd.add("-loop");
////            cmd.add("1");
////
////            cmd.add("-r");
////            cmd.add("1");
////            cmd.add("-i");
////
////            cmd.add(new File(imagepath).getCanonicalPath());
////            cmd.add("-i");
////            cmd.add(new File(musicpath).getCanonicalPath());
////
////            cmd.add("-b:a");
////            cmd.add("420k");
////            cmd.add("-strict");
////
////            cmd.add("experimental");
////            cmd.add("-shortest");
////            cmd.add("-f");
////
////            cmd.add("mp4");
////            cmd.add("-vb");
////            cmd.add("20M");
////
////            cmd.add("-r");
////            cmd.add("2");
////            cmd.add("-preset");
////            cmd.add("ultrafast");
////            cmd.add("-c:a");
////            cmd.add("aac");
//                val command = arrayOf("-y"
//                    ,"-loop","1"
//                    ,"-r","1"
//                    ,"-i", getImagePath(imageUrl,context)
//                    ,"-i",audiooutput
//                    ,"-b:a","420k"
//                    ,"-strict","experimental"
//                    ,"-shortest"
//                    ,"-f","mp4"
//                    ,"-vb","20M"
//                    ,"-r","2"
//                    ,"-preset","ultrafast"
//                    ,"-c:a","aac"
//                    ,outputpath)
//
//                val command2 = arrayOf("-y"
//                    ,"-i",audiooutput
//                    ,"-i", getImagePath(imageUrl,context)
//                    ,"-shortest"
//                    , outputpath)
//
//                val command3 = arrayOf(
//                    "-y"
//                    ,"-i", getImagePath(imageUrl,context)
//                    ,"-i",audiooutput
//                    ,"-c:a","aac"
//                    ,"-c:v","libx264"
//                    , outputpath)
//
//            executeWithArgumentsAsync(command){session ->
//                if(ReturnCode.isSuccess(session.returnCode)) {
//                    val post = Post(
//                        name = outputpath.substringAfterLast("/"),
//                        type = POST_TYPE_OVERLAY,
//                        url = outputpath
//                    )
//                    trySend(post)
//                    coroutine.launch {
//                        EventBus.updateState(States.DONE_SUCCESS.toString())
//                    }
//                }else{
//                    coroutine.launch {
//                        EventBus.updateState(States.DONE_FAILED.toString())
//                    }
//                }
//            }
//
//        }else{
//            coroutine.launch {
//                EventBus.updateState(States.DONE_FAILED.toString())
//            }
//        }
//    }
//
//
//
//
//    awaitClose {
//
//    }
//}

//private fun generateFilename(type : String) : String{
//    if (type == typeVideo){
//        var count = 0
//        var file : File
//        do {
//            file = File(DESTINATIONPATH,"Video$count.mp4")
//            count++
//        }while (file.exists())
//
//        createFolderAndFile(file)
//        return file.path
//    }else{
//        var count = 0
//        var file : File
//        do {
//            file = File(DESTINATIONPATH,"Audio$count.mp3")
//            count++
//        }while (file.exists())
//
//        createFolderAndFile(file)
//        return file.path
//    }
//}
//
//private fun createFolderAndFile(newfile : File){
//    val file = File(DESTINATIONPATH)
//    if (!file.exists()) {
//        file.mkdir()
//    }
//    newfile.createNewFile()
//}
//
//fun getImagePath(uri : String,appContext: Context) : String{
//    var filePath: String?
//
//    val _uri: Uri = Uri.parse(uri)
//
//    if ("content" == _uri.scheme) {
//        val cursor: Cursor = appContext.contentResolver.query(_uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)!!
//        cursor.moveToFirst()
//        Log.d("tag", _uri.toString())
//        filePath = cursor.getString(0)
//        cursor.close()
//    } else {
//        filePath = _uri!!.path
//    }
//
//
//
//    return filePath!!
//}

 fun convertSecondsToHMmSs(miliSeconds: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(miliSeconds).toInt() % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(miliSeconds).toInt() % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds).toInt() % 60
    return when {
        hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, seconds)
        minutes > 0 -> String.format("%02d:%02d", minutes, seconds)
        seconds > 0 -> String.format("00:%02d", seconds)
        else -> {
            "00:00"
        }
    }
}

fun File.writeBitmap(bitmap: Bitmap,format : Bitmap.CompressFormat,quality : Int){
    outputStream().use { outputStream ->
        bitmap.compress(format,quality,outputStream)
        outputStream.flush()
        outputStream.close()
    }
}

fun isDefaultPlaylistsSaved(context: Context) : Boolean{
    val sharedPreferences = context.getSharedPreferences("Default playlists",Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(ISSAVED,false)
}

fun updateDefaultPlaylistsStatus(context: Context){
    val sharedPreferences = context.getSharedPreferences("Default playlists",Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean(ISSAVED,true).apply()
}

fun getPlayMode(context: Context) : Int{
    val sharedPreferences = context.getSharedPreferences("PlayMode",Context.MODE_PRIVATE)
    return sharedPreferences.getInt("playmode",ExoPlayer.REPEAT_MODE_ONE)
}

fun savedPlayMode(playmode : Int,context: Context){
    val sharedPreferences = context.getSharedPreferences("PlayMode",Context.MODE_PRIVATE)
    sharedPreferences.edit().putInt("playmode",playmode).apply()
}
fun findPlaylistbyname(playlists : List<Playlist>, name : String) : Playlist {
    var foundplaylist = Playlist(name = "")
    playlists.forEach { playlist ->
        if (playlist.name == name){
            foundplaylist = playlist
        }
    }

    return foundplaylist
}

fun StartSong(song: Song,context: Context,songlist : List<Song>){
    Intent(context,ForegroundService :: class.java).also {intent ->
        intent.action = MusicActions.start.toString()
        intent.putExtra(ForegroundIntentExtras.LIST.toString(), Songs(songlist))
        intent.putExtra(ForegroundIntentExtras.ID.toString(),song.url)
        context.startForegroundService(intent)
    }
}
fun Navigate(isSameSong : Boolean,navController: NavHostController,song: Song,context: Context,songlist: List<Song>){
    if (isSameSong){
        navController.navigate(Routes.CurrentSongScreen())
    }else{
        navController.navigate(Routes.CurrentSongScreen())
        StartSong(song,context,songlist)
    }
}

fun likeSong(likedsong: Song, context: Context){
    likedsong.liked = !likedsong.liked
    saveListOfModifiedSongs(context,likedsong)
}

private fun saveListOfModifiedSongs(context: Context,song: Song){
    val localRepo = LocalRepoImpl()
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    coroutineScope.launch(Dispatchers.IO) {
        localRepo.saveSong(context,song)
    }
}

fun QueueSong(song: Song,context: Context){
    Intent(context, ForegroundService :: class.java).also { intent ->
        intent.action = MusicActions.queue.toString()
        intent.putExtra(QUEDE_SONG_URL.toString(),song.url)
        context.startForegroundService(intent)
    }
}

fun ShareSong(song: Song,context: Context){
    val intent = Intent(Intent.ACTION_SEND).apply {
        var uri = Uri.parse(song.url)
        setType(audioMMIMETYPE)
        putExtra(Intent.EXTRA_STREAM,uri)
        putExtra(Intent.EXTRA_COMPONENT_NAME,song.title)
    }

    context.startActivity(Intent.createChooser(intent,null))
}





