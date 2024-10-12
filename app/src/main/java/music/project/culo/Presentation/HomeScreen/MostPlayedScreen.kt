package music.project.culo.Presentation.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import music.project.culo.Presentation.Components.Songlist
import music.project.culo.Presentation.Components.options
import music.project.culo.Presentation.Components.playlist
import music.project.culo.Domain.Model.Song
import music.project.culo.Presentation.Components.categorizer
import music.project.culo.Presentation.Components.customButton
import music.project.culo.Utils.QueueSong
import music.project.culo.Utils.ShareSong
import music.project.culo.Utils.TestTags
import music.project.culo.Utils.likeSong

@Composable
fun MostPlayedScreen(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel
){
    Scaffold {paddingValues ->
        midSectionMostPlayed(paddingValues = paddingValues,navController,homeScreenViewModel)
    }
}

@Composable
fun midSectionMostPlayed(
    paddingValues: PaddingValues, navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel
){
    val showPlaylist = rememberSaveable {
        mutableStateOf(false)
    }

    val showOptions = rememberSaveable {
        mutableStateOf(false)
    }

    val isplaylist = rememberSaveable {
        mutableStateOf(false)
    }

    val currentOptionedSong = rememberSaveable {
        mutableStateOf(Song())
    }

    val scrollstate = rememberLazyListState()
    val songList = homeScreenViewModel.songlist.collectAsState()
    val context = LocalContext.current

    val filteredList = songList.value
        .filter { song ->
            song.plays > 0
        }
        .sortedBy { song ->
            song.plays
        }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.background,
                    Color.DarkGray
                )
            )
        )
        .padding(
            top = paddingValues.calculateTopPadding() + 10.dp,
            start = 5.dp,
            end = 5.dp,
        )){
        Column(modifier = Modifier.fillMaxSize()
            , horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)) {

            //List of songs
            Songlist(modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth(), songlist = filteredList.reversed(),
                navController = navController,
                onShowOptions = {isPlaylist,song ->
                    showOptions.value = true
                    isplaylist.value = isPlaylist
                    currentOptionedSong.value = song
                }, ScreenViewModel = homeScreenViewModel,
                scrollstate = scrollstate)

        }

        //options
        if (showOptions.value){
            options(modifier = Modifier.align(Alignment.Center),
                onShowPlaylist = { showPlaylist.value = true },
                onShare = {
                    ShareSong(currentOptionedSong.value,context)
                    showOptions.value = false
                },
                onAddToLiked = {
                    likeSong(currentOptionedSong.value,context)
                    showOptions.value = false
                },
                isplaylist = isplaylist.value, onRemoveOptions = {
                    showOptions.value = false
                }, onAddToQueue = {
                    QueueSong(currentOptionedSong.value,context)
                    showOptions.value = false
                })
        }

        //playlist list
        if (showPlaylist.value){
            playlist(modifier = Modifier.align(Alignment.Center),
                onAddSOngToPlayList = {playlist ->
                    val newSet = mutableSetOf<Song>()

                    newSet.addAll(playlist.songs.songList)
                    newSet.add(currentOptionedSong.value)
                    playlist.songs.songList = newSet.toList()

                    homeScreenViewModel.updatePlaylist(context,playlist)
                }){
                showPlaylist.value = false
            }
        }
    }
}