package music.project.culo.View

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController

@Composable
fun Top3PostDetails(navController: NavHostController){
    Scaffold(topBar = {
        topSection(navController = navController)
    },
        floatingActionButton = {
            customButton(text = " Create Post", modifier = Modifier) {
                navController.navigateUp()
            }
        }) {paddingValues ->
        midSectionTop3PostDetails(paddingValues =paddingValues )
    }
}

@Composable
fun midSectionTop3PostDetails(paddingValues: PaddingValues){
    val title = rememberSaveable {
        mutableStateOf("Tyla")
    }

    val artist1 = rememberSaveable {
        mutableStateOf("Tyla")
    }

    val song1 = rememberSaveable {
        mutableStateOf("water 1")
    }

    val artist2 = rememberSaveable {
        mutableStateOf("Tyla")
    }

    val song2 = rememberSaveable {
        mutableStateOf("water 2")
    }

    val artist3 = rememberSaveable {
        mutableStateOf("Tyla")
    }

    val song3 = rememberSaveable {
        mutableStateOf("water 3")
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
        val (playlisttitle,song1details,song2details,song3details) = createRefs()

        //playlist list title
        Text(
            modifier = Modifier.constrainAs(playlisttitle){
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
            },
            text = title.value,
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondary)


            // song 1
        Song(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(song1details) {
                    top.linkTo(
                        playlisttitle.bottom,
                        margin = 30.dp
                    )
                },
            artist = artist1.value,
            song = song1.value,
            number = 1,
            onChangeArtistValue = {newvalue ->
                artist1.value = newvalue
            }
        ) {newvalue ->
            song1.value = newvalue
        }

        // song 2
        Song(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(song2details) {
                    top.linkTo(
                        song1details.bottom,
                        margin = 30.dp
                    )
                },
            artist = artist2.value,
            song = song2.value,
            number = 2,
            onChangeArtistValue = {newvalue ->
                artist2.value = newvalue
            }
        ) {newvalue ->
            song2.value = newvalue
        }

        // song 3
        Song(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(song3details) {
                    top.linkTo(
                        song2details.bottom,
                        margin = 30.dp
                    )
                },
            artist = artist3.value,
            song = song3.value,
            number = 3,
            onChangeArtistValue = {newvalue ->
                artist3.value = newvalue
            }
        ) {newvalue ->
            song3.value = newvalue
        }
    }




}

@Composable
fun Song(modifier: Modifier,
           artist : String,
           song : String,
           number: Int,
           onChangeArtistValue : (newvalue : String) -> Unit,
           onChangeSongValue : (newvalue : String) -> Unit){

    Column(modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        //Title
        Text(text = "Number ${number}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondary)

        //artist name text field
        TextField(
            modifier = Modifier
                .background(Color.Transparent)
                .clip(RoundedCornerShape(16)),
            value = artist,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground
            ),
            singleLine = true,
            onValueChange = {newText ->
                onChangeArtistValue.invoke(newText)
            }, placeholder = {
                Text(text = "Artist ${number} Name",
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

        TextField(
            modifier = Modifier
                .background(Color.Transparent)
                .clip(RoundedCornerShape(16)),
            value = song,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground
            ),
            singleLine = true,
            onValueChange = {newText ->
                onChangeSongValue.invoke(newText)
            }, placeholder = {
                Text(text = "Song ${number} Name",
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