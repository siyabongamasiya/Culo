package music.project.culo.View

import Fonts.fontFamily
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun PostDetailsScreen(navController: NavHostController){
    Scaffold (topBar = {
        topSection(navController = navController)
    }, floatingActionButton = {
        customButton(text = "Next", modifier = Modifier) {
            navController.navigate(Routes.AudioCuttingScreen(""))
        }
    }){paddingValues ->
        midSectionPostDetails(paddingValues = paddingValues,navController)
    }
}

@Composable
fun midSectionPostDetails(paddingValues: PaddingValues,
                          navController: NavHostController){
    val artistName = rememberSaveable {
        mutableStateOf("Tyla")
    }

    val songName = rememberSaveable {
        mutableStateOf("Water")
    }

    val caption = rememberSaveable {
        mutableStateOf("")
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
                onValueChange = {newText ->
                    artistName.value = newText
                }, placeholder = {
                    Text(text = "Artist Name",
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
                onValueChange = {newText ->
                    songName.value = newText
                }, placeholder = {
                    Text(text = "Song Name",
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

            //caption text field
            TextField(
                modifier = Modifier
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(16)),
                value = caption.value,
                textStyle = TextStyle(
                    fontFamily = fontFamily2,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                ),
                onValueChange = {newText ->
                    caption.value = newText
                }, placeholder = {
                    Text(text = "Caption(optional)",
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