package music.project.culo.Presentation.CurrentSongScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import music.project.culo.Presentation.Components.customButton
import music.project.culo.Presentation.Components.playlist
import music.project.culo.Presentation.Components.requestImages
import music.project.culo.Presentation.Routes
import music.project.culo.Utils.SongManager
import music.project.culo.Utils.ShareSong
import music.project.culo.Utils.TestTags
import music.project.culo.Utils.iconSize
import music.project.culo.Utils.likeSong
import music.project.culo.Utils.operatorOptions
import music.project.culo.Utils.songOperatorsize

@Composable
fun CurrentSongScreen(
    navController: NavHostController,
    currentSongScreenViewModel: CurrentSongScreenViewModel
){
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        currentSongScreenViewModel.getSongs(context)
    }
    Scaffold (topBar = {
        topSection(navController)
    }, modifier = Modifier.testTag(TestTags.CurrentSongScreen.tag),
        bottomBar = {
            //controls
            Controls(modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                )
                .padding(10.dp),currentSongScreenViewModel)
        }){paddingValues ->
        midSectionCurrentSong(paddingValues = paddingValues,navController,currentSongScreenViewModel)
    }
}

@Composable
fun topSection(navController: NavController,
               isplaylist : Boolean = false,
               isAudioCutting : Boolean = false,
               playlistname : String = "",
               onAudioScreenBackPressed : () -> Unit = {},
               onEditPlaylist : (newName : String) -> Unit = {}){

    val isModifyingTitle = rememberSaveable {
        mutableStateOf(false)
    }

    val newTitle = rememberSaveable {
        mutableStateOf("")
    }

    val playListname = rememberSaveable {
        mutableStateOf(playlistname)
    }

    Box (modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.10f)
        .background(MaterialTheme.colorScheme.surface)
        .padding(5.dp)){
        Icon(
            modifier = Modifier
                .size(iconSize.dp)
                .clickable {
                    navController.navigateUp()
                    if (isAudioCutting){
                        onAudioScreenBackPressed.invoke()
                    }
                }
                .align(Alignment.CenterStart),
            tint = MaterialTheme.colorScheme.onSecondary,
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "back")




        when(isModifyingTitle.value){
            false -> {
                if (isplaylist) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = playListname.value,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondary,
                            textDecoration = TextDecoration.Underline
                        )

                        if (playlistname != "Liked") {
                            Icon(
                                modifier = Modifier
                                    .size(iconSize.dp)
                                    .clickable {
                                        isModifyingTitle.value = true
                                    },
                                imageVector = Icons.Default.Create,
                                contentDescription = "Edit"
                            )
                        }

                    }

                }
            }

            true -> {
                if (isplaylist) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        ///new playlist text field
                        TextField(
                            modifier = Modifier
                                .weight(0.7f)
                                .clip(RoundedCornerShape(16)),
                            value = newTitle.value,
                            singleLine = true,
                            onValueChange = { newText ->
                                newTitle.value = newText
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

                        customButton(
                            "Edit", modifier = Modifier
                                .weight(0.3f)
                        ) {
                            isModifyingTitle.value = false
                            if (newTitle.value.isNotEmpty()) {
                                playListname.value = newTitle.value
                                onEditPlaylist.invoke(newTitle.value)
                            }

                        }

                    }
                }
            }
        }


    }
}

@Composable
fun midSectionCurrentSong(paddingValues: PaddingValues,
                          navController: NavHostController,
                          currentSongScreenViewModel: CurrentSongScreenViewModel){

    val showPlaylist = rememberSaveable {
        mutableStateOf(false)
    }

    val showImages = rememberSaveable {
        mutableStateOf(false)
    }

    val currentSongDetails = SongManager.currentSongDetails.collectAsState()

    val context = LocalContext.current


    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(
            top = paddingValues.calculateTopPadding(),
            start = 10.dp,
            end = 10.dp
        )) {

        val (operators,songstatus,playlists) = createRefs()


        //operators
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .horizontalScroll(rememberScrollState())
            .constrainAs(operators) {
                top.linkTo(
                    parent.top,
                    margin = 10.dp
                )
            },
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically){

            songOperator(
                modifier = Modifier
                    .size(songOperatorsize.dp)
                    .weight(0.5f)
                    .background(
                        shape = RoundedCornerShape(16),
                        brush = Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    )
                    .padding(5.dp),
                name = operatorOptions.Add_to_Playlist.option,
                icon = {
                       Icon(
                           modifier = Modifier.size(iconSize.dp),
                           imageVector = Icons.Default.Add,
                           contentDescription = "add to playlist")
                }){
                showPlaylist.value = true
            }

            songOperator(
                modifier = Modifier
                    .size(songOperatorsize.dp)
                    .weight(0.5f)
                    .background(
                        shape = RoundedCornerShape(16),
                        brush = Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    )
                    .padding(5.dp),
                name = operatorOptions.Share.option,
                icon = {
                    Icon(
                        modifier = Modifier.size(iconSize.dp),
                        imageVector = Icons.Default.Share,
                        contentDescription = "share song")
                }){
                ShareSong(currentSongDetails.value.currentSong,context)
            }

//            songOperator(
//                modifier = Modifier
//                    .size(songOperatorsize.dp)
//                    .weight(0.25f)
//                    .semantics {
//                        contentDescription = TestTags.CreatePost.tag
//                    }
//                    .background(
//                        shape = RoundedCornerShape(16),
//                        brush = Brush.linearGradient(
//                            listOf(
//                                MaterialTheme.colorScheme.surface,
//                                MaterialTheme.colorScheme.secondaryContainer
//                            )
//                        )
//                    )
//                    .padding(5.dp),
//                name = operatorOptions.Create_post.option,
//                icon = {
//                    Icon(
//                        modifier = Modifier.size(iconSize.dp),
//                        imageVector = Icons.Default.Upload,
//                        contentDescription = "create post Icon")
//                }){
//                if (currentSongDetails.value.isPlaying) {
//                    currentSongScreenViewModel.pauseplayCurrentSong(context)
//                }
//                val artist = currentSongDetails.value.currentSong.artist
//                val title = currentSongDetails.value.currentSong.title
//
//                navController.navigate(Routes.PostDetailsScreen(artist,title))
//            }

//            songOperator(modifier = Modifier
//                .size(songOperatorsize.dp)
//                .weight(0.25f)
//                .background(
//                    shape = RoundedCornerShape(16),
//                    brush = Brush.linearGradient(
//                        listOf(
//                            MaterialTheme.colorScheme.surface,
//                            MaterialTheme.colorScheme.secondaryContainer
//                        )
//                    )
//                )
//                .padding(5.dp),
//                name = operatorOptions.Overlay_On_Image.option,
//                icon = {
//                    Icon(
//                        modifier = Modifier.size(iconSize.dp),
//                        imageVector = Icons.Default.ContentCopy,
//                        contentDescription = "image overlay")
//                }){
//                showImages.value = true
//                if (currentSongDetails.value.isPlaying) {
//                    currentSongScreenViewModel.pauseplayCurrentSong(context)
//                }
//                Toast.makeText(context,"Please wait for images to show!!",Toast.LENGTH_LONG).show()
//            }
        }

        //status area
        Column(modifier = Modifier
            .constrainAs(songstatus) {
                top.linkTo(
                    operators.bottom
                )

                bottom.linkTo(
                    parent.bottom,
                    margin = 200.dp
                )
                //centerTo(parent)
            }
            .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                modifier = Modifier
                    .clickable {
                        val likedsong = currentSongDetails.value.currentSong
                        likeSong(likedsong, context)
                    }
                    .size((iconSize * 3).dp),
                imageVector = if (!currentSongDetails.value.currentSong.liked) Icons.Default.FavoriteBorder else Icons.Filled.Favorite,
                tint = if (!currentSongDetails.value.currentSong.liked) Color.Black else Color.Red,
                contentDescription = "liked/Unliked")

            Column(modifier = Modifier
                .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = currentSongDetails.value.currentSong.artist,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary)
                Text(text = currentSongDetails.value.currentSong.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary)

            }

        }


        //playlist list
        if (showPlaylist.value){
            playlist(modifier = Modifier.constrainAs(playlists){
                centerTo(parent)
            }){
                showPlaylist.value = false
            }
        }

        //images
        if(showImages.value){
            requestImages(navController,currentSongDetails.value.currentTimeMs){
                showImages.value = false
            }
        }

    }
}

@Composable
fun songOperator(modifier: Modifier,
                 name : String ,
                 icon : @Composable() ()-> Unit,
                 onClick : (() -> Unit)){


    Column(
        modifier = modifier.clickable { onClick.invoke() },
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {

        icon.invoke()
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondary)

    }
}

@Composable
fun Controls(modifier: Modifier,currentSongScreenViewModel: CurrentSongScreenViewModel){
    ConstraintLayout(modifier = modifier) {
        val (times,progress,controls,playmode) = createRefs()
        val currentSongDetails = SongManager.currentSongDetails.collectAsStateWithLifecycle()
        val context = LocalContext.current


        Row(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(times) {
                top.linkTo(
                    parent.top,
                    margin = 10.dp
                )
            },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                modifier = Modifier.semantics {
                    stateDescription = currentSongDetails.value.currentTime
                },
                text = currentSongDetails.value.currentTime,
                style = MaterialTheme.typography.bodyMedium)
            Text(text = currentSongDetails.value.currentTotalTime,
                style = MaterialTheme.typography.bodyMedium)
        }

        //progress bar
        Slider(
            value = (currentSongDetails.value.currentTimeMs.toFloat()/currentSongDetails.value.currentTotalTimeMs.toFloat()),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.onSecondary,
                activeTrackColor = MaterialTheme.colorScheme.onSecondary,
                inactiveTrackColor = Color.DarkGray
            ),
            onValueChangeFinished = {

            },
            onValueChange = {percentage->
                val newPosition = currentSongDetails.value.currentSong.duration.toLong()*percentage
                currentSongScreenViewModel.SeekTo(newPosition.toLong(),context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(progress) {
                    top.linkTo(times.bottom)
                }
                .testTag(TestTags.CurrentSongSlider.tag)
        )

        //controls
        val playpauseIcon = if(currentSongDetails.value.isPlaying){
            Icons.Default.Pause
        }else{
            Icons.Default.PlayArrow
        }
        Row (modifier = Modifier
            .fillMaxWidth()
            .constrainAs(controls) {
                top.linkTo(
                    progress.bottom,
                    margin = 20.dp
                )
                centerHorizontallyTo(parent)
            }, horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){

            Icon(
                modifier = Modifier
                    .clickable {
                        currentSongScreenViewModel.previousSong(context)
                    }
                    .size(iconSize.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "previous")

            Icon(
                modifier = Modifier
                    .clickable {
                        currentSongScreenViewModel.pauseplayCurrentSong(context)
                    }
                    .size(iconSize.dp),
                imageVector = playpauseIcon,
                contentDescription = "play")

            Icon(
                modifier = Modifier
                    .clickable {
                        currentSongScreenViewModel.nextSong(context)
                    }
                    .size(iconSize.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "next")

        }

        //playmode
        val playmodeIcon = if (currentSongDetails.value.currentRepeatMode == ExoPlayer.REPEAT_MODE_ALL && !currentSongDetails.value.shuffleModeOn){
            Icons.Default.Repeat
        }else if (currentSongDetails.value.currentRepeatMode == ExoPlayer.REPEAT_MODE_ONE && !currentSongDetails.value.shuffleModeOn){
            Icons.Default.RepeatOne
        }else{
            Icons.Default.Shuffle
        }

        Icon(
            modifier = Modifier
                .clickable {
                    currentSongScreenViewModel.shuffle(context)
                }
                .size(iconSize.dp)
                .constrainAs(playmode) {
                    top.linkTo(
                        controls.bottom,
                        margin = 20.dp
                    )
                    centerHorizontallyTo(parent)
                },
            imageVector = playmodeIcon,
            contentDescription = "playmode")
    }
}



