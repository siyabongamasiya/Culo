package music.project.culo.Utils

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import music.project.culo.Presentation.AudioCuttingScreen.AudioCuttingSceenViewModel
import music.project.culo.Presentation.CurrentSongScreen.CurrentSongScreenViewModel
import music.project.culo.Presentation.HomeScreen.HomeScreenViewModel
import music.project.culo.Presentation.PlaylistListScreen.PlaylistListViewModel
import music.project.culo.Presentation.PostDetailsScreen.PostDetailsScreenViewModel
import music.project.culo.Presentation.PostViewer.PostViewerViewModel

object ViewmodelFactory {


    @Composable
    fun createViewModel(type : String) : ViewModel? {
        return when (type) {
            AUDIO_CUTTING_SCREEN_VIEWMODEL -> hiltViewModel<AudioCuttingSceenViewModel>()

            CURRENT_SONG_SCREEN_VIEWMODEL -> hiltViewModel<CurrentSongScreenViewModel>()

            HOME_SCREEN_VIEWMODEL -> hiltViewModel<HomeScreenViewModel>()

            PLAYLIST_LIST_SCREEN_VIEWMODEL -> hiltViewModel<PlaylistListViewModel>()

            POST_DETAILS_SCREEN_VIEWMODEL -> hiltViewModel<PostDetailsScreenViewModel>()

            POST_VIEWER_SCREEN_VIEWMODEL -> hiltViewModel<PostViewerViewModel>()

            else -> null
        }
    }
}