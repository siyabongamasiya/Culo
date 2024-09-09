package music.project.culo.Presentation.PostDetailsScreen

import Fonts.fontFamily2
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import music.project.culo.Presentation.Components.customButton
import music.project.culo.Presentation.Routes
import music.project.culo.Utils.SongManager

@Composable
fun PostDetailsScreen(
    navController: NavHostController,
    postDetailsScreenViewModel: PostDetailsScreenViewModel,
    artist: String,
    title: String
){
    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current
    var caption by rememberSaveable {
        mutableStateOf("")
    }

    var captionIsVisible by rememberSaveable {
        mutableStateOf(true)
    }

    val currentSongDetails = SongManager.currentSongDetails.collectAsStateWithLifecycle()

    Scaffold (floatingActionButton = {
        customButton(text = "Next", modifier = Modifier) {
            if (caption.isEmpty()){
                captionIsVisible = false

                coroutineScope.launch {
                    delay(500)
                    val url = postDetailsScreenViewModel.CreateImage(view)
                    captionIsVisible = true
                    navController.navigate(Routes.AudioCuttingScreen(url,currentSongDetails.value.currentTimeMs))
                }
            }else{
                val url = postDetailsScreenViewModel.CreateImage(view)
                navController.navigate(Routes.AudioCuttingScreen(url,currentSongDetails.value.currentTimeMs))
            }
        }
    }){paddingValues ->
        midSectionPostDetails(paddingValues = paddingValues,
            caption = caption,
            captionIsVisible = captionIsVisible,
            artist = artist,
            title = title){newString ->
            caption = newString
        }
    }
}

@Composable
fun midSectionPostDetails(paddingValues: PaddingValues,
                          caption : String,
                          captionIsVisible : Boolean,
                          artist: String,
                          title: String,
                          onCaptionChange : (newvalue : String) -> Unit,){
    val artistName = rememberSaveable {
        mutableStateOf(artist)
    }

    val songName = rememberSaveable {
        mutableStateOf(title)
    }

    //belong to playlist
    val numberOnList = rememberSaveable {
        mutableIntStateOf(3)
    }

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.surface
                )
            )
        )
        .padding(
            top = paddingValues.calculateTopPadding() + 10.dp,
            start = 10.dp,
            end = 10.dp,
            bottom = paddingValues.calculateBottomPadding() + 60.dp
        )) {

        val (details,number,button) = createRefs()

        //details
        Column(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(details) {
                top.linkTo(
                    parent.top,
                    margin = 10.dp
                )
                start.linkTo(parent.start)
            },
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            //artist name text field
            TextField(
                modifier = Modifier
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(16)),
                value = artistName.value,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true,
                onValueChange = { newText ->
                    artistName.value = newText
                }, placeholder = {
                    Text(
                        text = "Artist Name",
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
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            //song name text field
            TextField(
                modifier = Modifier
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(16)),
                value = songName.value,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true,
                onValueChange = { newText ->
                    songName.value = newText
                }, placeholder = {
                    Text(
                        text = "Song Name",
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
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            if (captionIsVisible) {
                //caption text field
                TextField(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .clip(RoundedCornerShape(16)),
                    value = caption,
                    textStyle = TextStyle(
                        fontFamily = fontFamily2,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    ),
                    onValueChange = { newText ->
                        onCaptionChange.invoke(newText)
                    }, placeholder = {
                        Text(
                            text = "Caption(optional)",
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
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

            }
        }

        //number
        Text(
            modifier = Modifier.constrainAs(number){
                bottom.linkTo(parent.bottom,
                    margin = 60.dp)
                centerHorizontallyTo(parent)
            },
            text = "Number ${numberOnList.value} most played of 200",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground)



    }
}