package music.project.culo

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import music.project.culo.Presentation.AppNavHost
import music.project.culo.Utils.TestTags
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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