package music.project.culo.Presentation.HomeScreen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import music.project.culo.Domain.Model.Post
import music.project.culo.R
import music.project.culo.Utils.iconSize
import music.project.culo.Utils.playlistsurfaceSize
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.VideoFrameDecoder
import music.project.culo.Presentation.Routes
import java.io.File


@Composable
fun CreatedVideos(navController: NavHostController,homeScreenViewModel: HomeScreenViewModel){
    Scaffold {paddingValues->
        midSectionCreatedVideos(paddingValues = paddingValues,navController,homeScreenViewModel)
    }
}

@Composable
fun midSectionCreatedVideos(paddingValues: PaddingValues,
                            navController: NavHostController,
                            homeScreenViewModel: HomeScreenViewModel){
    val posts = homeScreenViewModel.postlist.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(key1 = Unit) {
        homeScreenViewModel.collectPosts(context)
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
        )) {

        val (grid) = createRefs()

        if (posts.value.size > 0) {
            LazyVerticalGrid(modifier = Modifier
                .constrainAs(grid) {
                    top.linkTo(
                        parent.top,
                        margin = paddingValues.calculateTopPadding() + 50.dp
                    )
                    bottom.linkTo(
                        parent.bottom,
                        margin = paddingValues.calculateBottomPadding() + 50.dp
                    )
                }
                .fillMaxSize(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(posts.value.toList()) { post ->
                    videoItem(post = post,navController,homeScreenViewModel)
                }
            }
        }else{
            Box(modifier = Modifier.fillMaxSize()){
                Text(modifier = Modifier.align(Alignment.Center),
                    text = "No Posts Yet!!")
            }
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun videoItem(post : Post,navHostController: NavHostController,homeScreenViewModel: HomeScreenViewModel){
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(VideoFrameDecoder.Factory())
        }.crossfade(true)
        .build()

    val painter = rememberAsyncImagePainter(
        model = Uri.fromFile(File(post.url)),
        imageLoader= imageLoader)


    Box(
        modifier = Modifier
            .clickable {
                navHostController.navigate(Routes.PostViewerScreen(post.url))
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
            .width(playlistsurfaceSize.dp)
            .height(playlistsurfaceSize.dp)) {


        Image(
            painter = painter,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            contentDescription = "video image")

        Row (modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        Color.LightGray,
                        Color.LightGray
                    )
                ), alpha = 0.8f,
                shape = RoundedCornerShape(50)
            )
            .padding(10.dp)
            .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){

            Text(text = post.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black)

            Icon(
                modifier = Modifier
                    .clickable {
                        homeScreenViewModel.deletePost(context,post)
                    }
                    .size(iconSize.dp),
                imageVector = Icons.Default.Delete,
                contentDescription = "delete icon")
        }

    }
}