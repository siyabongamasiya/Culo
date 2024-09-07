package music.project.culo.Presentation.PostViewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import music.project.culo.Presentation.Components.customButton
import music.project.culo.Presentation.CurrentSongScreen.topSection
import music.project.culo.ui.theme.CuloTheme

@Composable
fun DrawPostViewerScreen(
    navController: NavHostController, url: String, postViewerViewModel: PostViewerViewModel
){
    CuloTheme {
        Scaffold(topBar = {
            topSection(navController = navController)
        }) {paddingvalues ->

            midSectionVideoViewer(paddingValues = paddingvalues, url = url, postViewerViewModel = postViewerViewModel)
        }
    }

}


@Composable
fun midSectionVideoViewer(paddingValues: PaddingValues,url : String,postViewerViewModel: PostViewerViewModel){
    val context = LocalContext.current

    val exoPlayer = ExoPlayer.Builder(context).build()
    val mediaitem = MediaItem.fromUri(url)


    LaunchedEffect(Unit) {
        exoPlayer.setMediaItem(mediaitem)
        exoPlayer.prepare()
    }

    Column(modifier = Modifier
        .fillMaxSize().background(
            brush = Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.background,
                    Color.DarkGray
                )
            )
        )
        .padding(
            top = paddingValues.calculateTopPadding(),
            bottom = 56.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f)
        )

        customButton(text = "Share",
            modifier = Modifier.weight(0.1f)) {
            postViewerViewModel.SharePost(url,context)
        }

    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}