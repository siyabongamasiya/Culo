package music.project.culo.View

import android.graphics.drawable.Icon
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import music.project.culo.Utils.TestTags
import music.project.culo.Utils.iconSize
import music.project.culo.Utils.operatorOptions
import music.project.culo.Utils.songOperatorsize

@Composable
fun CurrentSongScreen(navController: NavController){
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
                .padding(10.dp))
        }){paddingValues ->
        midSectionCurrentSong(paddingValues = paddingValues,navController)
    }
}

@Composable
fun topSection(navController: NavController,
               isplaylist : Boolean = false,
               playlistname : String = ""){

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
                            }
                        }

                    }
                }
            }
        }


    }
}

@Composable
fun midSectionCurrentSong(paddingValues: PaddingValues,navController: NavController){

    val showPlaylist = rememberSaveable {
        mutableStateOf(false)
    }

    val showImages = rememberSaveable {
        mutableStateOf(false)
    }



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
                    .weight(0.25f)
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
                    .weight(0.25f)
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

            }

            songOperator(
                modifier = Modifier
                    .size(songOperatorsize.dp)
                    .weight(0.25f)
                    .semantics {
                        contentDescription = TestTags.CreatePost.tag
                    }
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
                name = operatorOptions.Create_post.option,
                icon = {
                    Icon(
                        modifier = Modifier.size(iconSize.dp),
                        imageVector = Icons.Default.Upload,
                        contentDescription = "create post Icon")
                }){
                navController.navigate(Routes.PostDetailsScreen(""))
            }

            songOperator(modifier = Modifier
                .size(songOperatorsize.dp)
                .weight(0.25f)
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
                name = operatorOptions.Overlay_On_Image.option,
                icon = {
                    Icon(
                        modifier = Modifier.size(iconSize.dp),
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "image overlay")
                }){
                showImages.value = true
            }


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
                modifier = Modifier.size((iconSize*3).dp),
                imageVector = Icons.Default.FavoriteBorder,
                tint = Color.Black,
                contentDescription = "liked/Unliked")

            Column(modifier = Modifier
                .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = "Tyla",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary)
                Text(text = "Water",
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
fun Controls(modifier: Modifier){
    ConstraintLayout(modifier = modifier) {
        val (times,progress,controls,playmode) = createRefs()

        val currentTimeMs = rememberSaveable {
            mutableStateOf(120000)
        }

        var totalTimeMs = 240000

        var currentTime = rememberSaveable {
            mutableStateOf("")
        }

        var totalTime = rememberSaveable {
            mutableStateOf("")
        }

        var playMode = ""



        LaunchedEffect(key1 = currentTimeMs.value) {
            //calculate total time
            val Ttotalsecs = totalTimeMs/1000
            val TMinutes = Ttotalsecs/60
            val Tsecs = Ttotalsecs%60


            //formating
            var Tminutesformatted = ""
            var Tsecsformatted  = ""

            //adding zeros if less than 9
            if (TMinutes <= 9){
                Tminutesformatted = "0${TMinutes}"
            }else{
                Tminutesformatted = "${TMinutes}"
            }

            if (Tsecs <= 9){
                Tsecsformatted = "0${Tsecs}"
            }else{
                Tsecsformatted = "${Tsecs}"
            }

            totalTime.value = "$Tminutesformatted : $Tsecsformatted"



            //calculate current time
            val Ctotalsecs = currentTimeMs.value/1000
            val CMinutes = Ctotalsecs/60
            val Csecs = Ctotalsecs%60

            //formating
            var Cminutesformatted = ""
            var Csecsformatted  = ""

            //adding zeros if less than 9
            if (CMinutes <= 9){
                Cminutesformatted = "0${CMinutes}"
            }else{
                Cminutesformatted = "${CMinutes}"
            }

            if (Csecs <= 9){
                Csecsformatted = "0${Csecs}"
            }else{
                Csecsformatted = "${Csecs}"
            }

            currentTime.value = "$Cminutesformatted : $Csecsformatted"
        }


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
                    stateDescription = currentTime.value
                },
                text = currentTime.value,
                style = MaterialTheme.typography.bodyMedium)
            Text(text = totalTime.value,
                style = MaterialTheme.typography.bodyMedium)
        }

        //progress bar
        Slider(
            value = (currentTimeMs.value.toFloat()/totalTimeMs.toFloat()),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.onSecondary,
                activeTrackColor = MaterialTheme.colorScheme.onSecondary,
                inactiveTrackColor = Color.DarkGray
            ),
            onValueChangeFinished = {

            },
            onValueChange = {percentage->
                            currentTimeMs.value = (totalTimeMs*percentage).toInt()
            },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(progress) {
                    top.linkTo(times.bottom)
                }
                .testTag(TestTags.CurrentSongSlider.tag)
        )

        //controls
        val playpauseIcon : ImageVector
        if (true){
            playpauseIcon = Icons.Default.PlayArrow
        }else{
            playpauseIcon = Icons.Default.Pause
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
                modifier = Modifier.size(iconSize.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "previous")

            Icon(
                modifier = Modifier.size(iconSize.dp),
                imageVector = playpauseIcon,
                contentDescription = "play")

            Icon(
                modifier = Modifier.size(iconSize.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "next")

        }

        //playmode
        val playmodeIcon : ImageVector

        when(playMode){

            "shuffle" -> playmodeIcon = Icons.Default.Shuffle

            "repeat All" -> playmodeIcon = Icons.Default.Repeat

            "repeat One" -> playmodeIcon = Icons.Default.RepeatOne

            else ->{
                playmodeIcon = Icons.Default.Repeat
            }
        }
        Icon(
            modifier = Modifier
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



