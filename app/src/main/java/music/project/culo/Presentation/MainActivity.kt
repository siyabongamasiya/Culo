package music.project.culo.Presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import music.project.culo.Presentation.AudioCuttingScreen.AudioCuttingSceenViewModel
import music.project.culo.Presentation.AudioCuttingScreen.AudioCuttingScreen
import music.project.culo.Presentation.CurrentSongScreen.CurrentSongScreen
import music.project.culo.Presentation.CurrentSongScreen.CurrentSongScreenViewModel
import music.project.culo.Presentation.HomeScreen.HomeScreen
import music.project.culo.Presentation.HomeScreen.HomeScreenViewModel
import music.project.culo.Presentation.PlaylistListScreen.PlaylistListViewModel
import music.project.culo.Presentation.PlaylistListScreen.PlaylistlistScreen
import music.project.culo.Presentation.PostDetailsScreen.PostDetailsScreen
import music.project.culo.Presentation.PostDetailsScreen.PostDetailsScreenViewModel
import music.project.culo.Presentation.PostViewer.DrawPostViewerScreen
import music.project.culo.Presentation.PostViewer.PostViewerViewModel
import music.project.culo.Presentation.Top3PostDetailsScreen.Top3PostDetails
import music.project.culo.Utils.EventBus
import music.project.culo.Utils.States
import music.project.culo.ui.theme.CuloTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            CuloTheme {
                val context = LocalContext.current
                val navController = rememberNavController()

                val state = EventBus.state.collectAsStateWithLifecycle(initialValue = States.DONE_SUCCESS.toString())

                if(state.value == States.DONE_SUCCESS.toString()) {
                    AppNavHost(navController = navController)
                }else if(state.value == States.INPROGRESS.toString()){
                    AppNavHost(navController = navController)
                    Box (modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)){

                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.surface
                        )

                    }
                }else if(state.value == States.DONE_FAILED.toString()){
                    AppNavHost(navController = navController)
                    Toast.makeText(context, "Failed!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}

@Composable
fun AppNavHost(navController: NavHostController){
    val homeScreenViewModel = HomeScreenViewModel()
    val playlistListViewModel = PlaylistListViewModel()
    val currentSongScreenViewModel = CurrentSongScreenViewModel()
    val audioCuttingSceenViewModel = AudioCuttingSceenViewModel()
    val postDetailsScreenViewModel = PostDetailsScreenViewModel()
    val postViewerViewModel =  PostViewerViewModel()

    NavHost(navController = navController, startDestination = "Home"){

        composable("Home") {
            HomeScreen(navController,homeScreenViewModel)
        }

        composable<Routes.CurrentSongScreen>{
            CurrentSongScreen(navController,currentSongScreenViewModel)
        }

        composable<Routes.Playlist_listScreen> {
            val args = it.toRoute<Routes.Playlist_listScreen>()
            val playlistname = args.playlistname
            PlaylistlistScreen(navController,playlistname,playlistListViewModel)
        }

        composable<Routes.PostDetailsScreen> {
            val route = it.toRoute<Routes.PostDetailsScreen>()
            val artist = route.artist
            val title = route.title

            PostDetailsScreen(navController,postDetailsScreenViewModel,artist,title)
        }

        composable<Routes.AudioCuttingScreen> {
            val route = it.toRoute<Routes.AudioCuttingScreen>()
            val url = route.uri
            val currentTime = route.currentTime

            AudioCuttingScreen(navController,url,audioCuttingSceenViewModel,currentTime)
        }

        composable<Routes.Top3PostDetailsScreen> {
            Top3PostDetails(navController)
        }

        composable<Routes.PostViewerScreen> {
            val route = it.toRoute<Routes.PostViewerScreen>()
            val url = route.url


            DrawPostViewerScreen(navController = navController, url = url,postViewerViewModel)
        }
    }
}