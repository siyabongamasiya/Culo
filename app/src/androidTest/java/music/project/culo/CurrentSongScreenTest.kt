package music.project.culo

import androidx.compose.material3.SliderPositions
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertRangeInfoEquals
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToKey
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeRight
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHost
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import music.project.culo.Utils.TestTags
import music.project.culo.View.Controls
import music.project.culo.View.CurrentSongScreen
import music.project.culo.View.HomeScreen
import music.project.culo.View.PostDetailsScreen
import music.project.culo.View.Routes
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.State

@RunWith(AndroidJUnit4 :: class)
class CurrentSongScreenTest{

    @get : Rule
    val testRule = createComposeRule()
    private lateinit var navController: TestNavHostController


    @Before
    fun setUp(){
        testRule.setContent {
            navController = TestNavHostController(LocalContext.current) 
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            AppNavHost(navController = navController)
        }
    }

    @Test
    fun slideSlider_Assert_CorrectTimeIsDisplayedOnCurrentTimeText(){
        //Arrange
        val progress = 0.6f
        var currentTime = convertTime(progress)


        //Act
        testRule.onNodeWithTag(TestTags.CurrentSongSlider.tag)
            .performSemanticsAction(SemanticsActions.SetProgress){progresser->
                progresser.invoke(0.6f)
            }

        //Assert
        testRule.onNode(hasStateDescription(currentTime), useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun pressCreatePost_Assert_rightScreenIsShown(){
        //Arrange
        //Act
        testRule.onNodeWithContentDescription(TestTags.CreatePost.tag, useUnmergedTree = true).performClick()
        //Assert
        val currentScreen = navController.currentBackStackEntry?.destination?.route
        assertEquals(currentScreen, "PostDetails")
    }

    @Test
    fun pressAddToPlaylist_Assert_OptionsAreShown() {
        //arrange
        //act
        testRule.onNodeWithTag(TestTags.Kebab.tag, useUnmergedTree = true).performClick()
        //assert
        testRule.onNodeWithTag(TestTags.Playlists.tag).assertExists()
    }

    private fun convertTime(progress : Float) : String{
        var currentTime : String
        var currentTimeMs = 240000*progress

        //calculate current time
        val Ctotalsecs = currentTimeMs/1000
        val CMinutes = Ctotalsecs/60
        val Csecs = Ctotalsecs%60

        //formating
        var Cminutesformatted = ""
        var Csecsformatted  = ""

        //adding zeros if less than 9
        if (CMinutes <= 9){
            Cminutesformatted = "0${CMinutes.toInt()}"
        }else{
            Cminutesformatted = "${CMinutes.toInt()}"
        }

        if (Csecs <= 9){
            Csecsformatted = "0${Csecs.toInt()}"
        }else{
            Csecsformatted = "${Csecs.toInt()}"
        }

        currentTime = "$Cminutesformatted : $Csecsformatted"
        return currentTime
    }
}