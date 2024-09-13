package music.project.culo.Presentation.HomeScreen

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import music.project.culo.Data.local.FakeLocalRepo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenViewModelTest{

    private lateinit var homeScreenViewModel: HomeScreenViewModel
    private lateinit var fakeLocalRepo: FakeLocalRepo
    private lateinit var context: Context
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp(){
        fakeLocalRepo = FakeLocalRepo()
        homeScreenViewModel = HomeScreenViewModel(fakeLocalRepo)
        context = ApplicationProvider.getApplicationContext()
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun oncollect_items_items_are_collected_correctly()= runTest{
        //arrange
        //act
        homeScreenViewModel.getSongs(context)
        //assert
        homeScreenViewModel.songlist.test {
            val songlist = awaitItem()
            Truth.assertThat(songlist.size).isEqualTo(3)
        }

    }

    @After
    fun teardown(){
        Dispatchers.resetMain()
    }


}