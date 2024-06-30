package music.project.culo.View

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import music.project.culo.R
import music.project.culo.Utils.NormalOption
import music.project.culo.Utils.PlaylistOptions
import music.project.culo.Utils.SongOptions
import music.project.culo.Utils.TestTags
import music.project.culo.Utils.iconSize


@Composable
fun AllScreen(navController: NavHostController,
              currentSong : String){

    Scaffold {paddingValues ->
        midSectionAll(paddingValues = paddingValues,
            navController,
            currentSong)
    }
}

@Composable
fun midSectionAll(paddingValues: PaddingValues,
                  navController: NavHostController,
                  currentSong: String){
    val searchedText = rememberSaveable {
        mutableStateOf("")
    }

    val selectedCategory = rememberSaveable {
        mutableStateOf("C")
    }

    val showPlaylistButton = rememberSaveable {
        mutableStateOf(false)
    }

    val songList = rememberSaveable {
        mutableStateOf(listOf("","","",""))
    }

    val showCategorizer = rememberSaveable {
        mutableStateOf(true)
    }

    val showOptions = rememberSaveable {
        mutableStateOf(false)
    }

    val isplaylist = rememberSaveable {
        mutableStateOf(false)
    }

    val showPlaylist = rememberSaveable {
        mutableStateOf(false)
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

        val (searchfield,divider,songlist,categorizer,button,options,playlists) = createRefs()

        //search song text field
        TextField(
            modifier = Modifier
                .constrainAs(searchfield) {
                    top.linkTo(
                        parent.top,
                        margin = paddingValues.calculateTopPadding()
                    )
                    centerHorizontallyTo(parent)
                }
                .clip(RoundedCornerShape(16))
                .testTag(TestTags.AllSongsSearchBar.tag),
            value = searchedText.value,
            singleLine = true,
            onValueChange = {newText ->
                searchedText.value = newText
                if (searchedText.value.isNotEmpty()){
                    showPlaylistButton.value = true
                    showCategorizer.value = false
                }else{
                    showPlaylistButton.value = false
                    showCategorizer.value = true
                }
            }, placeholder = {
                Text(text = "Search Song",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            colors = TextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                focusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary
            )
        )

        //divider
        Divider(modifier = Modifier
            .constrainAs(divider) {
                top.linkTo(
                    searchfield.bottom,
                    margin = 10.dp
                )
            }
            .fillMaxWidth()
            .height(2.dp),
            color = Color.Black)

        //List of songs
        Songlist(modifier = Modifier
            .constrainAs(songlist) {
                top.linkTo(
                    divider.bottom,
                    margin = 10.dp
                )

                bottom.linkTo(parent.bottom,
                    margin = 60.dp)
            }
            .fillMaxWidth(), songlist = songList.value,
            onShowOptions = {isPlaylist ->
                showOptions.value = true
                isplaylist.value = isPlaylist
            }){
            navController.navigate(Routes.CurrentSongScreen(currentSong))
        }

        //categorizer
        if (showCategorizer.value) {
            categorizer(
                selectedCategory = selectedCategory.value,
                modifier = Modifier
                    .clip(RoundedCornerShape(16))
                    .constrainAs(categorizer) {
                        bottom.linkTo(
                            parent.bottom,
                            margin = 40.dp
                        )

                        centerHorizontallyTo(parent)
                    }
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(5.dp)
            ) { letter ->
                selectedCategory.value = letter
            }
        }

        //button
        if (showPlaylistButton.value) {
            customButton(
                "Create Playlist", modifier = Modifier
                    .constrainAs(button) {
                        bottom.linkTo(
                            parent.bottom,
                            margin = 50.dp
                        )
                        end.linkTo(parent.end)
                    }
                    .padding(10.dp)
                    .testTag(TestTags.CreatePlaylistButton.tag)
            ) {

            }
        }

        //options
        if (showOptions.value){
            options(modifier = Modifier.constrainAs(options){
                centerTo(parent)
            },
                onShowPlaylist = { showPlaylist.value = true },
                isplaylist = isplaylist.value) {
                showOptions.value = false
            }
        }

        //playlist list
        if (showPlaylist.value){
            playlist(modifier = Modifier.constrainAs(playlists){
                centerTo(parent)
            }){
                showPlaylist.value = false
            }
        }

    }
}

@Composable
fun Songlist(modifier: Modifier,
             isplaylist: Boolean = false,
             songlist : List<String>,
             onShowOptions : (isplaylist : Boolean) -> Unit = {},
             onclick: () -> Unit){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(songlist){song->
            songItem(song,
                isplaylist = isplaylist
                , OnShowOptions = {
                onShowOptions.invoke(isplaylist)
            }, onclick = onclick)
        }
    }
}

@Composable
fun songItem(song : String,
             isplaylist : Boolean = false
             ,OnShowOptions : (isplaylist : Boolean) -> Unit
             ,onclick: () -> Unit){

    Row(
        horizontalArrangement = Arrangement.Absolute.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onclick.invoke()
            }
            .clip(RoundedCornerShape(16))
            .background(brush = Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.colorScheme.secondaryContainer
                )
            ))
            .shadow(10.dp)
            .padding(10.dp)
            ) {
        Icon(painter = painterResource(id = R.drawable.logoimage),
            contentDescription = "pic",
            modifier = Modifier
                .size(iconSize.dp)
                .clip(RoundedCornerShape(50)))
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Tyla",
                style = MaterialTheme.typography.titleMedium)
            Text(text = "Water",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyMedium)
        }

        Icon(imageVector = Icons.Default.MoreVert,
            tint = MaterialTheme.colorScheme.onSecondary,
            contentDescription = "kebab",
            modifier = Modifier
                .clickable {
                    OnShowOptions.invoke(isplaylist)
                }
                .testTag(TestTags.Kebab.tag)
                .size(iconSize.dp))
    }
}

@Composable
fun categorizer(selectedCategory : String,
                modifier: Modifier,onSelectCategory: (category : String) -> Unit){

    val listofletters = listOf("A","B","C","D","E","F","G","H","I","J","K","L",
        "M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")

    LazyRow(modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)) {

        items(listofletters){letter->
            if (letter.equals(selectedCategory)){
                Text(text = letter,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Yellow,
                    modifier = Modifier.clickable {
                        onSelectCategory.invoke(letter)
                    })
            }else{
                Text(text = letter,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.clickable {
                        onSelectCategory.invoke(letter)
                    })
            }

        }
    }
}

@Composable
fun options(modifier: Modifier,
            isplaylist : Boolean = false,
            onDeleteFromPlaylist : () -> Unit = {},
            onShowPlaylist : () -> Unit,
            onRemoveOptions : () -> Unit){

    Box(modifier = modifier) {
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(
                focusable = true
            ),
            onDismissRequest = {
                onRemoveOptions.invoke()
            }) {

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16))
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .testTag(TestTags.Playlists.tag)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    modifier = Modifier.clickable {
                        onRemoveOptions.invoke()
                        onShowPlaylist.invoke()
                    },
                    text = SongOptions.Add_to_Playlist.option,
                    color = Color.Black)
                if(true) {
                    Text(
                        text = SongOptions.Add_to_Liked.option,
                        color = Color.Black
                    )
                }else {
                    Text(
                        text = SongOptions.Remove_from_Liked.option,
                        color = Color.Black
                    )
                }
                Text(text = SongOptions.Share.option,
                    color = Color.Black)
                Text(text = SongOptions.Add_to_Queue.option,
                    color = Color.Black)

                if (isplaylist){
                    Text(modifier = Modifier.clickable {
                        onDeleteFromPlaylist.invoke()
                    },
                        text = SongOptions.Delete.option,
                        color = Color.Black)
                }

            }

        }
    }


}

@Composable
fun playlist(modifier: Modifier,
             onRemovePlaylist: () -> Unit){

    val playlistlist = listOf("jazz","paino","hip hop","tyla")
    val scrollState = rememberScrollState()

    Box(modifier = modifier) {
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(
                excludeFromSystemGesture = true
            ),
            onDismissRequest = {
                onRemovePlaylist.invoke()
            }) {

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16))
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .verticalScroll(scrollState)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                playlistlist.forEach { playlist ->
                    Text(text = playlist,
                        color = Color.Black)
                }


            }

        }
    }
}

@Composable
fun customButton(text : String,
                 modifier: Modifier,
                 onclick : () -> Unit){

    ElevatedButton(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = { onclick.invoke() }) {
        Text(text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondary,)
    }

}