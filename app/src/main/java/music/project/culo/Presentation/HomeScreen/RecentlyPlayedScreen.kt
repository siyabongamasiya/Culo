package music.project.culo.Presentation.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Presentation.Components.Songlist
import music.project.culo.Presentation.Components.options
import music.project.culo.Presentation.Components.playlist
import music.project.culo.Domain.Model.Song
import music.project.culo.Presentation.Routes
import music.project.culo.Utils.PlaylistProvider
import music.project.culo.Utils.QueueSong
import music.project.culo.Utils.RECENTS_PLAYLIST
import music.project.culo.Utils.ShareSong
import music.project.culo.Utils.TestTags
import music.project.culo.Utils.likeSong

@Composable
fun RecentleyPlayedScreen(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel
){
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        homeScreenViewModel.getSongs(context)
    }

    Scaffold {paddingValues ->
        midSectionRecentleyPlayed(paddingValues = paddingValues,navController,homeScreenViewModel)
    }
}

@Composable
fun midSectionRecentleyPlayed(
    paddingValues: PaddingValues, navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel
){

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        PlaylistProvider.collectPlaylists(context)
    }
    val playlists = PlaylistProvider.playlists.collectAsState()
    var recents  = Playlist()

    playlists.value.forEach {playlist ->
        if (playlist.name == RECENTS_PLAYLIST){
            recents = playlist
        }
    }

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
    val songList = recents.songs.songList


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

            if (songList.size > 0) {
                //List of songs
                Songlist(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth(), songlist = songList.reversed(),
                    navController = navController,
                    onShowOptions = { isPlaylist, song ->
                        showOptions.value = true
                        isplaylist.value = isPlaylist
                        currentOptionedSong.value = song
                    }, ScreenViewModel = homeScreenViewModel,
                    scrollstate = scrollstate
                )
            }else{
                Box(modifier = Modifier.fillMaxSize()){
                    Text(modifier = Modifier.align(Alignment.Center),
                        text = "No Songs Yet!!")
                }
            }

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