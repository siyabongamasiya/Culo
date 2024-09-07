package music.project.culo.Presentation.HomeScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Presentation.Components.customButton
import music.project.culo.Presentation.PlaylistListScreen.PlaylistListViewModel
import music.project.culo.Presentation.Routes
import music.project.culo.R
import music.project.culo.Utils.PlaylistProvider
import music.project.culo.Utils.RECENTS_PLAYLIST
import music.project.culo.Utils.iconSize
import music.project.culo.Utils.playlistsurfaceSize

@Composable
fun PlaylistScreen(navController: NavHostController, homeScreenViewModel: HomeScreenViewModel){
    Scaffold {paddingValues->
        midSectionPlaylistScreen(paddingValues = paddingValues,navController,homeScreenViewModel)
    }

}

@Composable
fun midSectionPlaylistScreen(paddingValues: PaddingValues,
                             navController: NavHostController,
                             homeScreenViewModel: HomeScreenViewModel){
    val context = LocalContext.current
    val newPlaylist = rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        PlaylistProvider.collectPlaylists(context)
    }

    val playlists = PlaylistProvider.playlists.collectAsState()
    val filteredplaylists = playlists.value.filter { playlist ->
        playlist.name != RECENTS_PLAYLIST
    }

    Column(modifier = Modifier
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
            10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {



        //new playlist
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically) {

            ///new playlist text field
            TextField(
                modifier = Modifier
                    .weight(0.7f)
                    .clip(RoundedCornerShape(16)),
                value = newPlaylist.value,
                singleLine = true,
                onValueChange = {newText ->
                                newPlaylist.value = newText
                }, placeholder = {
                    Text(text = "Create playlist",
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

            ///edit button
            customButton(text = "Add", modifier = Modifier.weight(0.3f)) {
                if(newPlaylist.value.isNotEmpty()) {
                    val newplaylist = Playlist(name = newPlaylist.value)
                    homeScreenViewModel.addPlaylist(newplaylist,context)
                }else{
                    Toast.makeText(context,"Please enter the name for new Playlist!!",Toast.LENGTH_SHORT).show()
                }
            }

        }

        //playlist grid
        LazyVerticalGrid(modifier = Modifier
            .fillMaxSize(),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            items(filteredplaylists.toList()){playlist ->
                playlistItem(playlist = playlist,homeScreenViewModel){
                    navController.navigate(Routes.Playlist_listScreen(playlist.name))
                }
            }

        }
    }
}

@Composable
fun playlistItem(playlist: Playlist,
                 homeScreenViewModel: HomeScreenViewModel,
                 onGoToPlaylist : () -> Unit){
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(16),
                brush = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            )
            .clickable {
                onGoToPlaylist.invoke()
            }
            .width(playlistsurfaceSize.dp)
            .height(playlistsurfaceSize.dp)) {

        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            painter = painterResource(id = R.drawable.logoimage),
            contentDescription = "playlist image")

        Row (modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        Color.LightGray,
                        Color.LightGray
                    )
                ), alpha = 0.8f,
                shape = RoundedCornerShape(50)
            )
            .padding(10.dp)
            .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){

            Text(text = playlist.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black)

            if (playlist.name != "Liked") {
                Icon(
                    modifier = Modifier
                        .clickable {
                            homeScreenViewModel.deletePlaylist(context, playlist)
                        }
                        .size(iconSize.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete icon"
                )
            }
        }

    }
}