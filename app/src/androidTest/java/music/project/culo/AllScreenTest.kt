package music.project.culo

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.requestFocus
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import music.project.culo.Utils.TestTags
import music.project.culo.View.HomeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4 :: class)
class AllScreenTest {

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

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun TypeonSearch_Assert_CreatePlaylistButtonIsVisible_And_NotVisibleIfSearchIsEmpty(){
        //Arrange
        //Act
        testRule.onNodeWithTag(TestTags.AllSongsSearchBar.tag)
            .requestFocus()
            .performKeyInput {
                keyDown(Key.One)
            }
        //Assert
        testRule.onNodeWithTag(TestTags.CreatePlaylistButton.tag)
            .assertExists()

        //Act
        testRule.onNodeWithTag(TestTags.AllSongsSearchBar.tag)
            .requestFocus()
            .performKeyInput {
                keyDown(Key.Backspace)
            }

        //Assert
        testRule.onNodeWithTag(TestTags.CreatePlaylistButton.tag)
            .assertDoesNotExist()
    }
}