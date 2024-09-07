package music.project.culo.Domain.MergeLocalAndRoomSOngs

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import music.project.culo.Data.LocalRepositoryImpl.LocalRepoImpl
import music.project.culo.Domain.Model.Song

suspend fun MergeRoomAndLocal(context: Context,update : (listofmodifiedsong : List<Song>) -> Unit){
    val localRepo = LocalRepoImpl()
    val localflow = localRepo.getSongsFromDevice(context)
    val roomflow = localRepo.getSongsFromRoom(context)

    localflow.collect{locallist ->
        roomflow.collect{roomsongs ->
            roomsongs.forEach { roomsong ->
                locallist.forEach {localsong ->
                    if (localsong.url == roomsong.url) {
                        localsong.plays = roomsong.plays
                        localsong.liked = roomsong.liked
                    }
                }
            }

            update.invoke(locallist)
        }
    }
}