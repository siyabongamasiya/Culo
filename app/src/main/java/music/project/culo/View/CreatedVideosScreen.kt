package music.project.culo.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import music.project.culo.R
import music.project.culo.Utils.iconSize
import music.project.culo.Utils.playlistsurfaceSize


@Composable
fun CreatedVideos(navController: NavController){
    Scaffold {paddingValues->
        midSectionCreatedVideos(paddingValues = paddingValues,navController)
    }
}

@Composable
fun midSectionCreatedVideos(paddingValues: PaddingValues,navController: NavController){
    val videos = rememberSaveable {
        mutableStateOf(listOf("video title 1",
            "video title 2",
            "video title 3",
            "video title 4",
            "video title 5"))
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

        LazyVerticalGrid(modifier = Modifier.constrainAs(grid){
            top.linkTo(parent.top,
                margin = paddingValues.calculateTopPadding() + 50.dp)
            bottom.linkTo(parent.bottom,
                margin = paddingValues.calculateBottomPadding() + 50.dp)
        }.fillMaxSize(),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(videos.value){video ->
                videoItem(video = video)
            }
        }
    }

}

@Composable
fun videoItem(video : String){
    Box(
        modifier = Modifier
        .background(shape = RoundedCornerShape(16),
            brush = Brush.linearGradient(
            listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.secondaryContainer
            )
        ))
        .width(playlistsurfaceSize.dp)
        .height(playlistsurfaceSize.dp)) {

        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            painter = painterResource(id = R.drawable.tyla),
            contentDescription = "video image")
        Row (modifier = Modifier
            .fillMaxWidth()
            .background(brush = Brush.linearGradient(
                listOf(
                    Color.LightGray,
                    Color.LightGray
                )
            ),alpha = 0.8f,
                shape = RoundedCornerShape(50))
            .padding(10.dp)
            .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){

            Text(text = video,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black)

            Icon(
                modifier = Modifier
                    .size(iconSize.dp),
                imageVector = Icons.Default.Delete,
                contentDescription = "delete icon")
        }

    }
}