package music.project.culo.Data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import music.project.culo.CuloApp
import music.project.culo.Domain.Model.Song
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
class SongDaoTest {

    @get:Rule
    val hiltrule = HiltAndroidRule(this)

    @Inject
    @Named("Database")
    lateinit var songDatabase: SongDatabase
    lateinit var songDAO: SongDAO

    @Before
    fun setup(){
        hiltrule.inject()
        songDAO = songDatabase.getDAO()
    }


    @Test
    fun saveSong() = runTest{
        //Arrange
        val song = Song(0,
            "some url",
            "go do it",
            "we ani",
            "102990",
            "3",
            8,
            true)

        //Act
        songDAO.SaveSong(song)
        songDAO.getSongs().test {
            val songlist = awaitItem()
            //Assert
            Truth.assertThat(song).isIn(songlist)
        }
    }

    @After
    fun teardown(){
        songDatabase.close()
    }


}