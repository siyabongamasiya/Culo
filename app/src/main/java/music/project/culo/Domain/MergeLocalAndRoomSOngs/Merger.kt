package music.project.culo.Domain.MergeLocalAndRoomSOngs

import android.content.Context
import music.project.culo.Data.local.LocalRepoImpl
import music.project.culo.Domain.LocalRepository.LocalRepo
import music.project.culo.Domain.Model.Song

suspend fun MergeRoomAndLocal(context: Context,
                              localRepo : LocalRepo,
                              update : (listofmodifiedsong : List<Song>) -> Unit){

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