package music.project.culo.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import music.project.culo.R
import music.project.culo.Utils.iconSize
import music.project.culo.Utils.playlistsurfaceSize

@Composable
fun PlaylistScreen(navController: NavHostController,
                   currentSong : String){
    Scaffold {paddingValues->
        midSectionPlaylistScreen(paddingValues = paddingValues,navController,currentSong)
    }

}

@Composable
fun midSectionPlaylistScreen(paddingValues: PaddingValues,navController: NavHostController,
                             currentSong : String){
    val newPlaylist = rememberSaveable {
        mutableStateOf("")
    }

    val playlists = rememberSaveable {
        mutableStateOf(listOf("Jazz","Hip Hop","piano","RnB"))
    }

    ConstraintLayout(modifier = Modifier
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
        )) {

        val (row,grid) = createRefs()


        //new playlist
        Row(modifier = Modifier
            .constrainAs(row){
                top.linkTo(parent.top,
                    margin = paddingValues.calculateTopPadding() + 10.dp)
            }
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

            }

        }

        //playlist grid
        LazyVerticalGrid(modifier = Modifier
            .constrainAs(grid) {
                top.linkTo(
                    row.bottom,
                    margin = 10.dp
                )
                bottom.linkTo(
                    parent.bottom,
                    margin = paddingValues.calculateBottomPadding() + 150.dp
                )
            },
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            items(playlists.value){playlist ->
                playlistItem(playlist = playlist){
                    navController.navigate(Routes.Playlist_listScreen(playlist))
                }
            }

        }
    }
}

@Composable
fun playlistItem(playlist : String,
                 onGoToPlaylist : () -> Unit){
    Box(
        modifier = Modifier
            .background(shape = RoundedCornerShape(16),
                brush = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                ))
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
            .background(brush = Brush.linearGradient(
                listOf(
                    Color.LightGray,
                    Color.LightGray
                )
            ),alpha = 0.8f,
                shape = RoundedCornerShape(50))
            .padding(10.dp)
            .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){

            Text(text = playlist,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black)

            Icon(
                modifier = Modifier
                    .size(iconSize.dp),
                imageVector = Icons.Default.Delete,
                contentDescription = "delete icon")
        }

    }
}