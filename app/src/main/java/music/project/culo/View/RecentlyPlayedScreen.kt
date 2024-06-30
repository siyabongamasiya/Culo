package music.project.culo.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import music.project.culo.Utils.NormalOption
import music.project.culo.Utils.PlaylistOptions
import music.project.culo.Utils.TestTags

@Composable
fun RecentleyPlayedScreen(navController: NavHostController,
                          currentSong : String){
    Scaffold {paddingValues ->
        midSectionRecentleyPlayed(paddingValues = paddingValues,navController,currentSong)
    }
}

@Composable
fun midSectionRecentleyPlayed(paddingValues: PaddingValues,navController: NavHostController,
                              currentSong : String){
    val showPlaylist = rememberSaveable {
        mutableStateOf(false)
    }

    val showOptions = rememberSaveable {
        mutableStateOf(false)
    }

    val isplaylist = rememberSaveable {
        mutableStateOf(false)
    }

    val songlist = rememberSaveable {
        mutableStateOf(listOf("","","","",""))
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
            top = paddingValues.calculateTopPadding() + 10.dp,
            start = 5.dp,
            end = 5.dp,
            bottom = paddingValues.calculateBottomPadding() + 60.dp
        ).testTag(TestTags.RecentlyPlayed.tag)) {

        val ( songList,options, playlists) = createRefs()

        //List of songs
        Songlist(modifier = Modifier
            .constrainAs(songList) {
                top.linkTo(
                    parent.top,
                    margin = 10.dp
                )
            }
            .fillMaxWidth(), songlist = songlist.value,
            onShowOptions = {isPlaylist ->
                showOptions.value = true
                isplaylist.value = isPlaylist
            }){
            navController.navigate(Routes.CurrentSongScreen(currentSong))
        }



        //options
        if (showOptions.value){
            options(modifier = Modifier.constrainAs(options){
                centerTo(parent)
            },
                onShowPlaylist = { showPlaylist.value = true },
                isplaylist = isplaylist.value) {
                showOptions.value = false
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