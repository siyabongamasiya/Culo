package music.project.culo.Presentation.PlaylistListScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Presentation.Components.Songlist
import music.project.culo.Presentation.Components.customButton
import music.project.culo.Domain.Model.Song
import music.project.culo.Presentation.Components.bottomSectionHome
import music.project.culo.Presentation.CurrentSongScreen.topSection
import music.project.culo.Presentation.Routes
import music.project.culo.Utils.SongManager
import music.project.culo.Utils.PlaylistProvider
import music.project.culo.Utils.QueueSong
import music.project.culo.Utils.ShareSong
import music.project.culo.Utils.iconSize
import music.project.culo.Utils.likeSong
import music.project.culo.ui.theme.selected_button

@Composable
fun PlaylistlistScreen(
    navController: NavHostController,
    playlistname: String,
    playlistListViewModel: PlaylistListViewModel
){
    val currentSongDetails = SongManager.currentSongDetails.collectAsState()
    val context = LocalContext.current

    if (currentSongDetails.value.hasStarted) {
        Scaffold(
            topBar = {
                topSection(
                    navController = navController,
                    isplaylist = true,
                    playlistname = playlistname
                ){newName ->
                    playlistListViewModel.updatePlaylistName(context,newName)
                }
            },
            bottomBar = {
                bottomSectionHome(currentSongDetails.value.currentSong,playlistListViewModel) {
                    navController.navigate(Routes.CurrentSongScreen(""))
                }
            }
        ) { paddingValues ->
            midSectionPlaylistList(
                paddingValues = paddingValues,
                navController,
                playlistListViewModel,
                playlistname
            )
        }
    }else{
        Scaffold(
            topBar = {
                topSection(
                    navController = navController,
                    isplaylist = true,
                    playlistname = playlistname
                ){newName ->
                    playlistListViewModel.updatePlaylistName(context,newName)
                }
            }
        ) { paddingValues ->
            midSectionPlaylistList(
                paddingValues = paddingValues,
                navController,
                playlistListViewModel,
                playlistname
            )
        }
    }
}

@Composable
fun midSectionPlaylistList(
    paddingValues: PaddingValues,
    navController: NavHostController,
    playlistListViewModel: PlaylistListViewModel,
    playlistname: String
){
    if (playlistname != "Liked"){
        DrawOtherList(
            paddingValues = paddingValues,
            navController = navController,
            playlistListViewModel = playlistListViewModel,
            playlistname = playlistname
        )
    }else{
        DrawLikedList(
            paddingValues = paddingValues,
            navController = navController,
            playlistListViewModel = playlistListViewModel,
            playlistname = playlistname
        )
    }
}

@Composable
fun searchEditor(modifier: Modifier,
                search: String,
                 onRemoveEditor : () -> Unit,
                onDeleteSearch : () -> Unit,
                onChangeSearch : () -> Unit,
                 onAddSearch : () -> Unit){
    val newSearch = rememberSaveable {
        mutableStateOf(search)
    }


    Box(modifier = modifier) {
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(
                focusable = true
            ),
            onDismissRequest = {
                onRemoveEditor.invoke()
            }) {

            Column(
                Modifier
                    .clip(RoundedCornerShape(16))
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                TextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16)),
                    value = newSearch.value,
                    singleLine = true,
                    onValueChange = { newText ->
                        newSearch.value = newText
                    }, placeholder = {
                        Text(
                            text = "New Title",
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

                if (!search.isEmpty()){
                    Row(modifier = Modifier
                        , horizontalArrangement = Arrangement.spacedBy(30.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        customButton(text = "Edit", modifier = Modifier) {
                            onChangeSearch.invoke()
                        }

                        customButton(text = "Delete", modifier = Modifier) {
                            onDeleteSearch.invoke()
                        }

                    }
                }else{
                    Row(modifier = Modifier
                        , horizontalArrangement = Arrangement.spacedBy(30.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        customButton(text = "Add", modifier = Modifier) {
                            onAddSearch.invoke()
                        }

                    }
                }


            }

        }
    }


}

@Composable
fun DrawOtherList(paddingValues: PaddingValues,
                  navController: NavHostController,
                  playlistListViewModel: PlaylistListViewModel,
                  playlistname: String){
    LaunchedEffect(key1 = Unit) {
        playlistListViewModel.collectPlaylists(playlistname)
    }

    val playList = playlistListViewModel.currentPlaylist.collectAsState()

    val currentOptionedSong = rememberSaveable {
        mutableStateOf(Song())
    }

    val showSearchEditor = rememberSaveable {
        mutableStateOf(false)
    }

    val currentSearch = rememberSaveable {
        mutableStateOf("")
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

    val scrollstate = rememberLazyListState()

    val context = LocalContext.current

//    //part of playlist
//    val searcheslist = rememberSaveable {
//        mutableStateOf(listOf("tyla","pop"))
//    }
//
//    //partofplaylist
//    val isSearchlist = rememberSaveable {
//        mutableStateOf(true)
//    }


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
            bottom = 10.dp
        )){

        Column(modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally){

            //searches to be added in version two of the app
//        if (isSearchlist.value){
//            Row (modifier = Modifier
//                .fillMaxWidth()
//                .constrainAs(searches) {
//                    top.linkTo(
//                        parent.top,
//                        margin = 5.dp
//                    )
//                },
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically){
//
//
//                LazyRow (
//                    horizontalArrangement = Arrangement.spacedBy(10.dp),
//                    verticalAlignment = Alignment.CenterVertically){
//
//                    items(searcheslist.value){search ->
//
//                        customButton(text = search, modifier = Modifier) {
//                            currentSearch.value = search
//                            showSearchEditor.value = true
//                        }
//
//                    }
//                }
//
//                Icon(
//                    modifier = Modifier.size(iconSize.dp)
//                        .background(color = selected_button,
//                            shape = RoundedCorn erShape(50)
//                        )
//                        .clickable {
//                            currentSearch.value = ""
//                            showSearchEditor.value = true
//                        },
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "add search")
//
//            }
//
//        }


            //playlist songs
            if (playList.value.songs.songList.size > 0) {
                Songlist(modifier = Modifier
                    .weight(0.94f)
                    .fillMaxWidth(),
                    songlist = playList.value.songs.songList,
                    ScreenViewModel = playlistListViewModel,
                    navController = navController,
                    isplaylist = true,
                    onShowOptions = { isPlaylist, song ->
                        showOptions.value = true
                        isplaylist.value = isPlaylist
                        currentOptionedSong.value = song
                    }, scrollstate = scrollstate
                )

            }else{
                Box(modifier = Modifier.fillMaxSize()){
                    Text(modifier = Modifier.align(Alignment.Center),
                        text = "Empty!!")
                }
            }

//        //post top 3 feature to be added on version 2 of the app
//        customButton(
//            "Post Top 3", modifier = Modifier
//                .padding(10.dp)
//        ) {
//            navController.navigate(Routes.Top3PostDetailsScreen(""))
//        }


            //edit search
            if (showSearchEditor.value) {
                searchEditor(modifier = Modifier
                    .padding(10.dp),
                    search = currentSearch.value,
                    onRemoveEditor = {
                        showSearchEditor.value = false
                    }
                    , onDeleteSearch = {
                        showSearchEditor.value = false
                    }, onChangeSearch = {
                        showSearchEditor.value = false
                    }, onAddSearch = {
                        showSearchEditor.value = false
                    })
            }
        }

        //options
        if (showOptions.value){
            music.project.culo.Presentation.Components.options(
                modifier = Modifier.align(Alignment.Center),
                isLiked = playList.value.name == "Liked",
                onShowPlaylist = { showPlaylist.value = true },
                onShare = {
                    ShareSong(currentOptionedSong.value,context)
                    showOptions.value = false
                },
                onAddToLiked = {
                    likeSong(currentOptionedSong.value,context)
                    showOptions.value = false
                }, onAddToQueue = {
                    QueueSong(currentOptionedSong.value,context)
                    showOptions.value = false
                },
                onDeleteFromPlaylist = {
                    val newSet = mutableSetOf<Song>()

                    newSet.addAll(playList.value.songs.songList)
                    Log.d("tag", "${newSet.size}")
                    newSet.remove(currentOptionedSong.value)
                    playList.value.songs.songList = newSet.toList()


                    playlistListViewModel.updatePlaylist(context,playList.value)
                    PlaylistProvider.collectPlaylists(context)
                    showOptions.value = false
                },
                isplaylist = isplaylist.value
                , onRemoveOptions = {
                    showOptions.value = false
                })
        }

        //playlist list
        if (showPlaylist.value){
            music.project.culo.Presentation.Components.playlist(
                modifier = Modifier.align(Alignment.Center),
                currentPlaylistName = playlistname,
                onAddSOngToPlayList = {playlist ->
                    val newSet = mutableSetOf<Song>()

                    newSet.addAll(playlist.songs.songList)
                    newSet.add(currentOptionedSong.value)
                    playlist.songs.songList = newSet.toList()

                    playlistListViewModel.updatePlaylist(context,playlist)
                }) {
                showPlaylist.value = false
            }
        }

    }
}

@Composable
fun DrawLikedList(paddingValues: PaddingValues,
                  navController: NavHostController,
                  playlistListViewModel: PlaylistListViewModel,
                  playlistname: String){
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        playlistListViewModel.getSongs(context = context)
    }

    val likedsongs = playlistListViewModel.songlist.collectAsState()

    val currentOptionedSong = rememberSaveable {
        mutableStateOf(Song())
    }

    val selectedCategory = rememberSaveable {
        mutableStateOf("A")
    }

    val showSearchEditor = rememberSaveable {
        mutableStateOf(false)
    }

    val currentSearch = rememberSaveable {
        mutableStateOf("")
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

    val scrollstate = rememberLazyListState()

    val coroutine = rememberCoroutineScope()

//    //part of playlist
//    val searcheslist = rememberSaveable {
//        mutableStateOf(listOf("tyla","pop"))
//    }
//
//    //partofplaylist
//    val isSearchlist = rememberSaveable {
//        mutableStateOf(true)
//    }


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
            bottom = 10.dp
        )){

        Column(modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally){

            //searches to be added in version two of the app
//        if (isSearchlist.value){
//            Row (modifier = Modifier
//                .fillMaxWidth()
//                .constrainAs(searches) {
//                    top.linkTo(
//                        parent.top,
//                        margin = 5.dp
//                    )
//                },
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically){
//
//
//                LazyRow (
//                    horizontalArrangement = Arrangement.spacedBy(10.dp),
//                    verticalAlignment = Alignment.CenterVertically){
//
//                    items(searcheslist.value){search ->
//
//                        customButton(text = search, modifier = Modifier) {
//                            currentSearch.value = search
//                            showSearchEditor.value = true
//                        }
//
//                    }
//                }
//
//                Icon(
//                    modifier = Modifier.size(iconSize.dp)
//                        .background(color = selected_button,
//                            shape = RoundedCorn erShape(50)
//                        )
//                        .clickable {
//                            currentSearch.value = ""
//                            showSearchEditor.value = true
//                        },
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "add search")
//
//            }
//
//        }


            //playlist songs
            if (likedsongs.value.isNotEmpty()) {
                Songlist(modifier = Modifier
                    .weight(0.94f)
                    .fillMaxWidth(),
                    songlist = likedsongs.value,
                    ScreenViewModel = playlistListViewModel,
                    navController = navController,
                    isplaylist = true,
                    onShowOptions = { isPlaylist, song ->
                        showOptions.value = true
                        isplaylist.value = isPlaylist
                        currentOptionedSong.value = song
                    }, scrollstate = scrollstate
                )

            }else{
                Box(modifier = Modifier.fillMaxSize()){
                    Text(modifier = Modifier.align(Alignment.Center),
                        text = "Empty!!")
                }
            }

//        //post top 3 feature to be added on version 2 of the app
//        customButton(
//            "Post Top 3", modifier = Modifier
//                .padding(10.dp)
//        ) {
//            navController.navigate(Routes.Top3PostDetailsScreen(""))
//        }


            //edit search
            if (showSearchEditor.value) {
                searchEditor(modifier = Modifier
                    .padding(10.dp),
                    search = currentSearch.value,
                    onRemoveEditor = {
                        showSearchEditor.value = false
                    }
                    , onDeleteSearch = {
                        showSearchEditor.value = false
                    }, onChangeSearch = {
                        showSearchEditor.value = false
                    }, onAddSearch = {
                        showSearchEditor.value = false
                    })
            }
        }

        //options
        if (showOptions.value){
            music.project.culo.Presentation.Components.options(
                modifier = Modifier.align(Alignment.Center),
                isLiked = true,
                onShowPlaylist = { showPlaylist.value = true },
                onShare = {
                    ShareSong(currentOptionedSong.value,context)
                    showOptions.value = false
                },
                onAddToLiked = {
                    likeSong(currentOptionedSong.value,context)
                    showOptions.value = false
                }, onAddToQueue = {
                    QueueSong(currentOptionedSong.value,context)
                    showOptions.value = false
                },
                onDeleteFromPlaylist = {
                    likeSong(currentOptionedSong.value,context)
                    showOptions.value = false
                },
                isplaylist = isplaylist.value
                , onRemoveOptions = {
                    showOptions.value = false
                })
        }

        //playlist list
        if (showPlaylist.value){
            music.project.culo.Presentation.Components.playlist(
                modifier = Modifier.align(Alignment.Center),
                currentPlaylistName = playlistname,
                onAddSOngToPlayList = {playlist ->
                    val newSet = mutableSetOf<Song>()

                    newSet.addAll(playlist.songs.songList)
                    newSet.add(currentOptionedSong.value)
                    playlist.songs.songList = newSet.toList()

                    playlistListViewModel.updatePlaylist(context,playlist)
                }) {
                showPlaylist.value = false
            }
        }

    }
}

