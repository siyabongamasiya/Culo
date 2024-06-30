package music.project.culo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import music.project.culo.View.AudioCuttingScreen
import music.project.culo.View.CurrentSongScreen
import music.project.culo.View.HomeScreen
import music.project.culo.View.PlaylistlistScreen
import music.project.culo.View.PostDetailsScreen
import music.project.culo.View.Routes
import music.project.culo.View.Top3PostDetails
import music.project.culo.ui.theme.CuloTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            CuloTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }


}

@Composable
fun AppNavHost(navController: NavHostController){
    NavHost(navController = navController, startDestination = "CurrentSong"){

        composable("Home") {
            HomeScreen(navController)
        }

        composable("CurrentSong") {
            CurrentSongScreen(navController)
        }

        composable<Routes.Playlist_listScreen> {
            val args = it.toRoute<Routes.Playlist_listScreen>()
            val playlistname = args.playlistname
            PlaylistlistScreen(navController,playlistname)
        }

        composable<Routes.PostDetailsScreen> {
            PostDetailsScreen(navController)
        }

        composable<Routes.AudioCuttingScreen> {
            AudioCuttingScreen(navController)
        }

        composable<Routes.Top3PostDetailsScreen> {
            Top3PostDetails(navController)
        }
    }
}
