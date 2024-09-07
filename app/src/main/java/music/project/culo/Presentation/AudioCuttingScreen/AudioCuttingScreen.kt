package music.project.culo.Presentation.AudioCuttingScreen

import Fonts.fontFamily
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import music.project.culo.Presentation.Components.customButton
import music.project.culo.Presentation.CurrentSongScreen.topSection
import music.project.culo.R
import music.project.culo.SongManager.SongManager
import music.project.culo.Utils.Instruction
import music.project.culo.Utils.OutofRange
import music.project.culo.Utils.iconSize

@Composable
fun AudioCuttingScreen(
    navController: NavHostController,
    url: String,
    audioCuttingSceenViewModel: AudioCuttingSceenViewModel,
    currentTime: Long
){

    audioCuttingSceenViewModel.setImageUrl(url)
    audioCuttingSceenViewModel.setCurrentTime(currentTime)

    BackHandler {
        audioCuttingSceenViewModel.resetTimeToCurrentTime()
        navController.navigateUp()
    }

    Scaffold (topBar = {
        topSection(navController = navController, isAudioCutting = true, onAudioScreenBackPressed = {
            audioCuttingSceenViewModel.resetTimeToCurrentTime()
        })
    },
        bottomBar = {
            audioCuttingControls(modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                )
                .padding(10.dp),navController,audioCuttingSceenViewModel)
        }){paddingValues ->
        midSectionAudioCutting(navController = navController, paddingValues = paddingValues)
    }
}

@Composable
fun midSectionAudioCutting(navController: NavHostController,
                           paddingValues: PaddingValues){


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
            top = paddingValues.calculateTopPadding(),
            start = 10.dp,
            end = 10.dp
        )) {
        
        val (instruction,add) = createRefs()
        
        
        //instruction
        Column(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(instruction) {
                top.linkTo(
                    parent.top,
                    margin = 10.dp
                )
            }, verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally) {
            
            Text(text = Instruction,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondary)
            
            Divider(modifier = Modifier.height(2.dp),
                color = Color.Black)
            
        }
        
        //Advertisement
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .constrainAs(add) {
                    top.linkTo(
                        instruction.bottom,
                        margin = 30.dp
                    )
                },
            contentScale = ContentScale.Fit,
            painter = painterResource(id = R.drawable.advert),
            contentDescription = "advert")
        
    }
    
}

@Composable
fun audioCuttingControls(modifier: Modifier,navController: NavHostController,audioCuttingSceenViewModel: AudioCuttingSceenViewModel){
    val currentSongDetails = SongManager.currentSongDetails.collectAsState()

    val currentRange = audioCuttingSceenViewModel.range.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showFiles by rememberSaveable {
        mutableStateOf(false)
    }

    ConstraintLayout(modifier = modifier) {
        val (times, progressbar,rangetext,title,button,controls) = createRefs()

        //title
        Text(
            modifier = Modifier.constrainAs(title){
                top.linkTo(parent.top,
                    margin = 5.dp)
                centerHorizontallyTo(parent)
            },
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                )){
                    append("${currentSongDetails.value.currentSong.artist} - " )
                }

                withStyle(style = SpanStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                )){
                    append(currentSongDetails.value.currentSong.title)
                }
            },
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondary)

        //current range
        Text(
            modifier = Modifier.constrainAs(rangetext){
                top.linkTo(title.bottom,
                    margin = 50.dp)
                bottom.linkTo(times.top,
                    margin = 10.dp)
                centerHorizontallyTo(parent)
            },
            text = currentRange.value,
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.labelMedium,
            color = if(currentRange.value == OutofRange) Color.Red else MaterialTheme.colorScheme.onSecondary)


        //times
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(times) {
                    bottom.linkTo(
                        progressbar.top,
                        margin = 10.dp
                    )
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = currentSongDetails.value.currentTime,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = currentSongDetails.value.currentTotalTime,
                style = MaterialTheme.typography.bodyMedium
            )
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
                audioCuttingSceenViewModel.calculateRange(currentSongDetails.value.currentTimeMs,
                    currentSongDetails.value.currentSong.duration.toLong())
            },
            onValueChange = { percentage ->
                val newPosition = currentSongDetails.value.currentSong.duration.toLong()*percentage
                audioCuttingSceenViewModel.SeekTo(newPosition.toLong())
            },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(progressbar) {
                    bottom.linkTo(
                        button.top,
                        margin = 10.dp
                    )
                }
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
                    progressbar.bottom,
                    margin = 20.dp
                )
                centerHorizontallyTo(parent)
            }, horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){

            Icon(
                modifier = Modifier
                    .clickable {
                        audioCuttingSceenViewModel.pauseplayCurrentSong(context)
                    }
                    .size(iconSize.dp),
                imageVector = playpauseIcon,
                contentDescription = "play")

        }

        //done button
        customButton(text = "Done", modifier = Modifier.constrainAs(button){
            bottom.linkTo(parent.bottom,
                margin = 10.dp)
            end.linkTo(parent.end,
                margin = 10.dp)
        }) {
            audioCuttingSceenViewModel.TrimAudio(
                currentSongDetails.value.currentSong.url,
                context
            )

            audioCuttingSceenViewModel.resetTimeToCurrentTime()

            navController.navigate("Home"){
                popUpTo(0){
                    inclusive = true
                }
            }
        }


    }
}