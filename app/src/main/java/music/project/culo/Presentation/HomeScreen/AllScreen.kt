package music.project.culo.Presentation.HomeScreen

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import music.project.culo.Domain.Model.Song
import music.project.culo.Presentation.Components.Songlist
import music.project.culo.Presentation.Components.categorizer
import music.project.culo.Presentation.Components.customButton
import music.project.culo.Presentation.Components.options
import music.project.culo.Presentation.Components.playlist
import music.project.culo.Presentation.Routes
import music.project.culo.Utils.EventBus
import music.project.culo.Utils.PlaylistProvider
import music.project.culo.Utils.QueueSong
import music.project.culo.Utils.ShareSong
import music.project.culo.Utils.States
import music.project.culo.Utils.TestTags
import music.project.culo.Utils.likeSong


@Composable
fun AllScreen(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel
){

    Scaffold {paddingValues ->
        midSectionAll(paddingValues = paddingValues,
            navController,homeScreenViewModel)
    }
}

@Composable
fun midSectionAll(
    paddingValues: PaddingValues,
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel
){
    val context = LocalContext.current

    val searchedText = rememberSaveable {
        mutableStateOf("")
    }

    val selectedCategory = rememberSaveable {
        mutableStateOf("A")
    }

    val showPlaylistButton = rememberSaveable {
        mutableStateOf(false)
    }

    val showCategorizer = rememberSaveable {
        mutableStateOf(true)
    }

    val showOptions = rememberSaveable {
        mutableStateOf(false)
    }

    val isplaylist = rememberSaveable {
        mutableStateOf(false)
    }

    val showPlaylist = rememberSaveable {
        mutableStateOf(false)
    }

    val currentOptionedSong = rememberSaveable {
        mutableStateOf(Song())
    }

    val scrollstate = rememberLazyListState()
    val songList = homeScreenViewModel.songlist.collectAsState()
    val coroutine = rememberCoroutineScope()

    val filteredList = songList.value
        .sortedBy { song ->
            song.title
        }
        .filter { song ->
            song.getSearchText().contains(searchedText.value,true)
        }

    LaunchedEffect(key1 = Unit) {
        homeScreenViewModel.getSongs(context)
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

            //search song text field
            TextField(
                modifier = Modifier
                    .clip(RoundedCornerShape(16))
                    .weight(0.12f)
                    .testTag(TestTags.AllSongsSearchBar.tag),
                value = searchedText.value,
                singleLine = true,
                onValueChange = {newText ->
                    searchedText.value = newText
                    if (searchedText.value.isNotEmpty()){
                        showPlaylistButton.value = true
                        showCategorizer.value = false
                    }else{
                        showPlaylistButton.value = false
                        showCategorizer.value = true
                    }
                }, placeholder = {
                    Text(text = "Search Song",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                colors = TextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    focusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary
                )
            )

            //divider
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = Color.Black
            )

            //List of songs
            Songlist(modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth(),
                songlist = filteredList,
                navController = navController,
                onShowOptions = {isPlaylist,song ->
                    showOptions.value = true
                    isplaylist.value = isPlaylist
                    currentOptionedSong.value = song
                }, ScreenViewModel = homeScreenViewModel,
                scrollstate = scrollstate)

            //categorizer
            if (showCategorizer.value) {
                categorizer(
                    selectedCategory = selectedCategory.value,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16))
                        .weight(0.06f)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(5.dp)
                ) { letter ->
                    selectedCategory.value = letter
                    coroutine.launch {
                        scrollstate.scrollToItem(homeScreenViewModel.findIndexOfFirstItem(letter))
                    }

                }
            }

            //button
            if (showPlaylistButton.value) {
                customButton(
                    "Create Playlist", modifier = Modifier
                        .padding(10.dp)
                        .testTag(TestTags.CreatePlaylistButton.tag)
                ) {

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






