package music.project.culo.Presentation.Components

import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Song
import music.project.culo.Presentation.HomeScreen.HomeScreenViewModel
import music.project.culo.Presentation.PlaylistListScreen.PlaylistListViewModel
import music.project.culo.Presentation.Routes
import music.project.culo.R
import music.project.culo.Utils.SongManager
import music.project.culo.Utils.Navigate
import music.project.culo.Utils.PlaylistProvider
import music.project.culo.Utils.QueueSong
import music.project.culo.Utils.SongOptions
import music.project.culo.Utils.TestTags
import music.project.culo.Utils.findPlaylistbyname
import music.project.culo.Utils.getArt
import music.project.culo.Utils.iconSize
import java.io.File
import kotlin.system.exitProcess

@Composable
fun customButton(text : String,
                 modifier: Modifier,
                 onclick : () -> Unit){

    ElevatedButton(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = { onclick.invoke() }) {
        Text(text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondary,)
    }

}


@Composable
fun playlist(modifier: Modifier,
             currentPlaylistName : String = "",
             onAddSOngToPlayList : (playlist : Playlist) -> Unit = {},
             onRemovePlaylist: () -> Unit){

    val playlistlist = PlaylistProvider.playlists.collectAsState()
    val scrollState = rememberScrollState()

    Box(modifier = modifier) {
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(
                excludeFromSystemGesture = true
            ),
            onDismissRequest = {
                onRemovePlaylist.invoke()
            }) {

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16))
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .verticalScroll(scrollState)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                playlistlist.value.forEach { playlist ->
                    if (currentPlaylistName != playlist.name) {
                        Text(
                            modifier = Modifier.clickable {
                                val playlist = findPlaylistbyname(playlistlist.value.toList(), playlist.name)
                                onAddSOngToPlayList.invoke(playlist)
                                onRemovePlaylist.invoke()
                            },
                            text = playlist.name,
                            color = Color.Black
                        )
                    }
                }


            }

        }
    }
}

@Composable
fun options(modifier: Modifier,
            isplaylist : Boolean = false,
            isLiked : Boolean = false,
            onDeleteFromPlaylist : () -> Unit = {},
            onShowPlaylist : () -> Unit = {},
            onRemoveOptions : () -> Unit = {},
            onAddToLiked : () -> Unit = {},
            onShare : () -> Unit = {},
            onAddToQueue : () -> Unit = {}){

    Box(modifier = modifier) {
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(
                focusable = true
            ),
            onDismissRequest = {
                onRemoveOptions.invoke()
            }) {

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16))
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .testTag(TestTags.Playlists.tag)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    modifier = Modifier.clickable {
                        onRemoveOptions.invoke()
                        onShowPlaylist.invoke()
                    },
                    text = SongOptions.Add_to_Playlist.option,
                    color = Color.Black)
                if (!isLiked) {
                    Text(
                        modifier = Modifier.clickable {
                            onAddToLiked.invoke()
                        },
                        text = SongOptions.Add_to_Liked.option,
                        color = Color.Black
                    )
                }
                Text(
                    modifier = Modifier.clickable {
                        onShare.invoke()
                    },
                    text = SongOptions.Share.option,
                    color = Color.Black)
                Text(
                    modifier = Modifier.clickable {
                        onAddToQueue.invoke()
                    },
                    text = SongOptions.Add_to_Queue.option,
                    color = Color.Black)

                if (isplaylist){
                    Text(modifier = Modifier.clickable {
                        onDeleteFromPlaylist.invoke()
                    },
                        text = SongOptions.Delete.option,
                        color = Color.Black)
                }

            }

        }
    }


}



@Composable
fun Songlist(modifier: Modifier,
             isplaylist: Boolean = false,
             songlist : List<Song>,
             onShowOptions : (isplaylist : Boolean,song : Song) -> Unit = {playlist,song -> },
             navController: NavHostController,
             ScreenViewModel: ViewModel,
             scrollstate: LazyListState?){
    val currentSongDetails = SongManager.currentSongDetails.collectAsStateWithLifecycle()



    if (ScreenViewModel is PlaylistListViewModel){
        val context = LocalContext.current
        LazyColumn(
            modifier = modifier,
            state = scrollstate!!,
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(songlist){song->
                songItem(song,
                    isplaylist = isplaylist
                    , OnShowOptions = {isAplaylist,providedSong ->
                        onShowOptions.invoke(isplaylist,providedSong)
                    }, onclick = {
                        Navigate(currentSongDetails.value.currentSong.url == song.url,
                            navController,
                            song,
                            context,
                            songlist)
                    })
            }
        }
    }else if (ScreenViewModel is HomeScreenViewModel){
        val context = LocalContext.current

        LazyColumn(
            modifier = modifier,
            state = scrollstate!!,
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(songlist, key = {song ->
                song.url
            }){song->
                songItem(song,
                    isplaylist = isplaylist
                    , OnShowOptions = {isAplaylist,providedSong ->
                        onShowOptions.invoke(isAplaylist,providedSong)
                    }, onclick = {
                        Navigate(currentSongDetails.value.currentSong.url == song.url,
                            navController,
                            song,
                            context,
                            songlist)
                    })
            }
        }
    }



}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun songItem(song : Song,
             isplaylist : Boolean = false
             ,OnShowOptions : (isplaylist : Boolean,song : Song) -> Unit
             ,onclick: () -> Unit){

    val currentSongDetails = SongManager.currentSongDetails.collectAsStateWithLifecycle()

    Row(
        horizontalArrangement = Arrangement.Absolute.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onclick.invoke()
            }
            .clip(RoundedCornerShape(16))
            .background(
                brush = if (currentSongDetails.value.currentSong.url != song.url) {
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                } else {
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            Color.Red
                        )
                    )
                }
            )
            .shadow(10.dp)
            .padding(10.dp)
    ) {

        val url = getArt(song.id)

        if(url != null) {
            GlideImage(
                modifier = Modifier
                    .size(iconSize.dp)
                    .weight(0.2f)
                    .clip(RoundedCornerShape(50)),
                model = url,
                failure = placeholder(R.drawable.logoimage),
                contentScale = ContentScale.Crop,
                contentDescription = "item image"
            )
        }else {
            Icon(
                painter = painterResource(id = R.drawable.logoimage),
                contentDescription = "pic",
                modifier = Modifier
                    .size(iconSize.dp)
                    .weight(0.2f)
                    .clip(RoundedCornerShape(50))
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
            .weight(0.6f)){
            Text(text = song.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium)
            Text(text = song.title,
                color = Color.LightGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium)
        }

        Icon(imageVector = Icons.Default.MoreVert,
            tint = MaterialTheme.colorScheme.onSecondary,
            contentDescription = "kebab",
            modifier = Modifier
                .clickable {
                    OnShowOptions.invoke(isplaylist, song)
                }
                .testTag(TestTags.Kebab.tag)
                .weight(0.2f)
                .size(iconSize.dp))
    }
}

@Composable
fun categorizer(selectedCategory : String,
                modifier: Modifier,onSelectCategory: (category : String) -> Unit){

    val listofletters = listOf("A","B","C","D","E","F","G","H","I","J","K","L",
        "M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")

    LazyRow(modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)) {

        items(listofletters){letter->
            if (letter.equals(selectedCategory)){
                Text(text = letter,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Yellow,
                    modifier = Modifier.clickable {
                        onSelectCategory.invoke(letter)
                    })
            }else{
                Text(text = letter,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.clickable {
                        onSelectCategory.invoke(letter)
                    })
            }

        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun requestPermission(content : @Composable()() -> Unit){
    val context = LocalContext.current
    val audioreadpermission = rememberPermissionState(permission = android.Manifest.permission.READ_EXTERNAL_STORAGE)

    val requestPermissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if(isGranted){
            Toast.makeText(context,"Permission granted!!",Toast.LENGTH_SHORT).show()
        }else{
            exitProcess(0)
        }
    }

    LaunchedEffect(key1 = audioreadpermission) {
        if (!audioreadpermission.status.isGranted){
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    if (audioreadpermission.status.isGranted){
        content.invoke()
    }else{
        Box(modifier = Modifier.fillMaxSize()){
            CircularProgressIndicator(modifier = Modifier
                .align(Alignment.Center),
                color = MaterialTheme.colorScheme.surface)
        }
    }

}

@Composable
fun ShowFiles(onChooseFile : (uriparam : Uri) -> Unit){
    val launcher = rememberLauncherForActivityResult(contract = CreateDocument("audio/mp3")) { uri ->
        onChooseFile.invoke(uri!!)
    }

    LaunchedEffect(key1 = Unit) {
        launcher.launch("audio")
    }
}

@Composable
fun bottomSectionHome(currentSong: Song,viewModel: ViewModel,onclick : ()  -> Unit){
    val context = LocalContext.current
    val currentSongDetails = SongManager.currentSongDetails.collectAsState()

    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onclick.invoke() }
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp)) {
        Icon(painter = painterResource(id = R.drawable.logoimage),
            contentDescription = "pic",
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .weight(0.2f)
                .clip(RoundedCornerShape(50)))
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(0.6f)){
            Text(modifier = Modifier
                .testTag(TestTags.Bottom_Bar.tag),
                text = currentSong.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium)
            Text(text = currentSong.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyMedium)
        }


        val playpauseIcon = if(currentSongDetails.value.isPlaying){
            Icons.Default.Pause
        }else{
            Icons.Default.PlayArrow
        }
        Icon(imageVector = playpauseIcon,
            tint = MaterialTheme.colorScheme.onSecondary,
            contentDescription = "play/pause",
            modifier = Modifier
                .clickable {
                    if (viewModel is HomeScreenViewModel) {
                        viewModel.pauseplayCurrentSong(context = context)
                    } else if (viewModel is PlaylistListViewModel) {
                        viewModel.pauseplayCurrentSong(context = context)
                    }
                }
                .width(50.dp)
                .height(50.dp)
                .weight(0.2f))
    }
}


@Composable
fun requestImages(navController: NavHostController,currentTime : Long,onFinish : () -> Unit){

    val imagesLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            navController.navigate(Routes.AudioCuttingScreen(uri = uri.toString(),currentTime))
        }
        onFinish.invoke()
    }

    LaunchedEffect(key1 = Unit) {
        imagesLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


}