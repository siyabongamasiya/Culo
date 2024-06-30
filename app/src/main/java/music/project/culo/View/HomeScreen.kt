package music.project.culo.View

import android.content.res.Resources.Theme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import music.project.culo.R
import music.project.culo.Utils.TestTags
import music.project.culo.ui.theme.CuloTheme
import music.project.culo.ui.theme.selected_button



@Composable
fun HomeScreen(navController: NavHostController){
    val currentsong = rememberSaveable() {
        mutableStateOf("current song")
    }

    Scaffold (bottomBar = {
        bottomSectionHome("current song"){
            navController.navigate(Routes.CurrentSongScreen(currentsong.value))
        }
    }){paddingValues ->

        Column(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
            midSectionHome(currentsong.value,navController)
        }
    }

}

@Composable
fun midSectionHome(currentSong : String,
                   navController: NavHostController){

    var pagerState = rememberPagerState {
        6
    }

    val couroutine = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.background,
                    Color.DarkGray
                )
            )
        )
        .padding(5.dp)) {
        ScrollableTabRow(selectedTabIndex = pagerState.currentPage,
            indicator = {
                if(pagerState.currentPage < 6){
                    TabRowDefaults.Indicator(modifier = Modifier.tabIndicatorOffset(it[pagerState.currentPage]),
                        color = selected_button)
                }
            },
            containerColor = MaterialTheme.colorScheme.background) {

            Tab(
                selected = pagerState.currentPage == 0,
                modifier = Modifier.padding(5.dp),
                onClick = {
                    couroutine.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
            ) {
                val bgcolor : Color
                if (pagerState.currentPage == 0){
                    bgcolor = selected_button
                }else{
                    bgcolor = MaterialTheme.colorScheme.secondaryContainer
                }

                Text(text = "All",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .background(
                            bgcolor,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(10.dp))
            }

            Tab(selected = pagerState.currentPage == 1,
                modifier = Modifier.padding(5.dp),
                onClick = {
                    couroutine.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }) {
                val bgcolor : Color
                if (pagerState.currentPage == 1){
                    bgcolor = selected_button
                }else{
                    bgcolor = MaterialTheme.colorScheme.secondaryContainer
                }
                Text(text = "Recently Played",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .background(
                            bgcolor,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(10.dp))
            }

            Tab(selected = pagerState.currentPage == 2,
                modifier = Modifier.padding(5.dp),
                onClick = { couroutine.launch {
                    pagerState.animateScrollToPage(2)
                } }) {
                val bgcolor : Color
                if (pagerState.currentPage == 2){
                    bgcolor = selected_button
                }else{
                    bgcolor = MaterialTheme.colorScheme.secondaryContainer
                }
                Text(text = "Recently Added",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .background(
                            bgcolor,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(10.dp))
            }

            Tab(selected = pagerState.currentPage == 3,
                modifier = Modifier.padding(5.dp),
                onClick = { couroutine.launch {
                    pagerState.animateScrollToPage(3)
                } }) {
                val bgcolor : Color
                if (pagerState.currentPage == 3){
                    bgcolor = selected_button
                }else{
                    bgcolor = MaterialTheme.colorScheme.secondaryContainer
                }
                Text(text = "Most Played",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .background(
                            bgcolor,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(10.dp))
            }

            Tab(selected = pagerState.currentPage == 4,
                modifier = Modifier.padding(5.dp),
                onClick = { couroutine.launch {
                    pagerState.animateScrollToPage(4)
                } }) {
                val bgcolor : Color
                if (pagerState.currentPage == 4){
                    bgcolor = selected_button
                }else{
                    bgcolor = MaterialTheme.colorScheme.secondaryContainer
                }

                Text(text = "Created Videos",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .background(
                            bgcolor,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(10.dp))
            }

            Tab(selected = pagerState.currentPage == 5,
                modifier = Modifier.padding(5.dp)
                    .testTag(TestTags.Playlists.tag),
                onClick = {
                    couroutine.launch {
                        pagerState.animateScrollToPage(5)
                    }}) {
                val bgcolor : Color
                if (pagerState.currentPage == 5){
                    bgcolor = selected_button
                }else{
                    bgcolor = MaterialTheme.colorScheme.secondaryContainer
                }

                Text(text = "Playlists",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .background(
                            bgcolor,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(10.dp))
            }
        }

        HorizontalPager(state = pagerState,
            modifier = Modifier.fillMaxSize()
                .testTag(TestTags.Horizontal_Pager.tag)) {index ->
            when(index){
                0 -> {
                    AllScreen(navController,currentSong)
                }

                1 -> {
                    RecentleyPlayedScreen(navController,currentSong)
                }

                2 -> {
                    RecentleyAddedScreen(navController,currentSong)
                }

                3 -> {
                    MostPlayedScreen(navController,currentSong)
                }

                4 -> {
                    CreatedVideos(navController)
                }

                5 -> {
                    PlaylistScreen(navController,currentSong)
                }
            }
        }
    }
}

@Composable
fun bottomSectionHome(currentSong: String,onclick : ()  -> Unit){
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onclick.invoke() }
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp)) {
        Icon(painter = painterResource(id = R.drawable.logoimage),
            contentDescription = "pic",
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(50)))
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text(modifier = Modifier
                .testTag(TestTags.Bottom_Bar.tag), text = "Tyla",
                style = MaterialTheme.typography.titleMedium)
            Text(text = "Water",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyMedium)
        }

        Icon(imageVector = Icons.Default.PlayArrow,
            tint = MaterialTheme.colorScheme.onSecondary,
            contentDescription = "play/pause",
            modifier = Modifier
                .width(50.dp)
                .height(50.dp))
    }
}

