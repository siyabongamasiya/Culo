package music.project.culo.Domain.MergeLocalAndRoomSOngs

import android.content.Context
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.zip
import music.project.culo.Data.local.LocalRepoImpl
import music.project.culo.Domain.LocalRepository.LocalRepo
import music.project.culo.Domain.Model.Song
import javax.inject.Inject

class GetSongs @Inject constructor(private val localRepo: LocalRepo){

    suspend operator fun invoke(context: Context,
                                update : (listofmodifiedsong : List<Song>) -> Unit){

        val localflow = localRepo.getSongsFromDevice(context)
        val roomflow = localRepo.getSongsFromRoom(context)

        roomflow.combine(localflow){roomsongs,localsongs ->
            roomsongs.forEach { roomsong ->
                localsongs.forEach {localsong ->
                    if (localsong.url == roomsong.url) {
                        localsong.plays = roomsong.plays
                        localsong.liked = roomsong.liked
                    }
                }
            }

            val sortedlist = localsongs.sortedBy {song ->
                song.title
            }
            sortedlist
        }.collect{updatedsongs ->
            update.invoke(updatedsongs)
        }
    }

}
