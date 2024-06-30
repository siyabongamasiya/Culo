package music.project.culo

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import music.project.culo.Utils.TestTags
import music.project.culo.View.HomeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomescreenTest {

    @get : Rule
    val testRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp(){
        testRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            HomeScreen(navController = navController)
        }
    }
    @Test
    fun SwipePager_Assert_CorrectScreenIsDrawn(){
        //Arrange

        //Act
        testRule.onNodeWithTag(TestTags.Horizontal_Pager.tag)
            .performTouchInput {
                swipeLeft()
            }
        //Assert
        testRule.onNodeWithTag(TestTags.RecentlyPlayed.tag)
            .assertExists()
    }

}