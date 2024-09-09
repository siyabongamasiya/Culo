package music.project.culo.Presentation.HomeScreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Presentation.Components.bottomSectionHome
import music.project.culo.Presentation.Components.requestPermission
import music.project.culo.Presentation.Routes
import music.project.culo.Utils.SongManager
import music.project.culo.Utils.PlaylistProvider
import music.project.culo.Utils.TestTags
import music.project.culo.Utils.isDefaultPlaylistsSaved
import music.project.culo.Utils.updateDefaultPlaylistsStatus
import music.project.culo.ui.theme.selected_button



@Composable
fun HomeScreen(navController: NavHostController, homeScreenViewModel: HomeScreenViewModel){
    val currentSongDetails = SongManager.currentSongDetails.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        val issaved = isDefaultPlaylistsSaved(context)
        if (!issaved) {
            homeScreenViewModel.addPlaylist(Playlist(name = "Liked"), context)
            updateDefaultPlaylistsStatus(context)
        }
    }

    requestPermission {
        val context = LocalContext.current
        PlaylistProvider.collectPlaylists(context)

        if(currentSongDetails.value.hasStarted) {

            Scaffold(bottomBar = {
                bottomSectionHome(currentSongDetails.value.currentSong,homeScreenViewModel) {
                    navController.navigate(Routes.CurrentSongScreen(""))
                }
            }) { paddingValues ->
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    )) {
                    midSectionHome(navController, homeScreenViewModel)
                }
            }

        }else{

            Scaffold { paddingValues ->
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())) {
                    midSectionHome(navController, homeScreenViewModel)
                }
            }

        }
    }
}

@Composable
fun midSectionHome(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel
){

    var pagerState = rememberPagerState {
        5
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
                if(pagerState.currentPage < 5){
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

//            Tab(selected = pagerState.currentPage == 4,
//                modifier = Modifier.padding(5.dp),
//                onClick = { couroutine.launch {
//                    pagerState.animateScrollToPage(4)
//                } }) {
//                val bgcolor = if (pagerState.currentPage == 4){
//                    selected_button
//                }else{
//                    MaterialTheme.colorScheme.secondaryContainer
//                }
//
//                Text(text = "Created Videos",
//                    style = MaterialTheme.typography.labelMedium,
//                    color = MaterialTheme.colorScheme.onSecondary,
//                    modifier = Modifier
//                        .background(
//                            bgcolor,
//                            RoundedCornerShape(16.dp)
//                        )
//                        .padding(10.dp))
//            }

            Tab(selected = pagerState.currentPage == 4,
                modifier = Modifier
                    .padding(5.dp)
                    .testTag(TestTags.Playlists.tag),
                onClick = {
                    couroutine.launch {
                        pagerState.animateScrollToPage(4)
                    }}) {
                val bgcolor : Color
                if (pagerState.currentPage == 4){
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
            modifier = Modifier
                .fillMaxSize()
                .testTag(TestTags.Horizontal_Pager.tag)) {index ->
            when(index){
                0 -> {
                    AllScreen(navController,homeScreenViewModel)
                }

                1 -> {
                    RecentleyPlayedScreen(navController,homeScreenViewModel)
                }

                2 -> {
                    RecentleyAddedScreen(navController,homeScreenViewModel)
                }

                3 -> {
                    MostPlayedScreen(navController,homeScreenViewModel)
                }

                4 -> {
                    PlaylistScreen(navController,homeScreenViewModel)
                }

            }
        }
    }
}


