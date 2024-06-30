package music.project.culo.View

import android.icu.text.StringSearch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import music.project.culo.Utils.NormalOption
import music.project.culo.Utils.PlaylistOptions
import music.project.culo.Utils.iconSize
import music.project.culo.ui.theme.selected_button

@Composable
fun PlaylistlistScreen(navController: NavHostController,
                        playlistname : String){
    Scaffold(
        topBar = {
            topSection(navController = navController,isplaylist = true, playlistname = playlistname)
        },
        bottomBar = {
            bottomSectionHome("current song"){
                navController.navigate(Routes.CurrentSongScreen(""))
            }
        }
    ) {paddingValues ->
        midSectionPlaylistList(paddingValues = paddingValues,navController)
    }
}

@Composable
fun midSectionPlaylistList(paddingValues: PaddingValues,
                           navController: NavHostController){

    val playList = rememberSaveable {
        mutableStateOf(listOf("","","",""))
    }

    val selectedCategory = rememberSaveable {
        mutableStateOf("A")
    }

    val showSearchEditor = rememberSaveable {
        mutableStateOf(false)
    }

    val currentSearch = rememberSaveable {
        mutableStateOf("")
    }

    val showPlaylist = rememberSaveable {
        mutableStateOf(false)
    }

    val showOptions = rememberSaveable {
        mutableStateOf(false)
    }

    val isplaylist = rememberSaveable {
        mutableStateOf(false)
    }

    //part of playlist
    val searcheslist = rememberSaveable {
        mutableStateOf(listOf("tyla","pop"))
    }

    //partofplaylist
    val isSearchlist = rememberSaveable {
        mutableStateOf(true)
    }

    ConstraintLayout (modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                listOf(
                    Color.DarkGray,
                    MaterialTheme.colorScheme.background
                )
            )
        )
        .padding(
            top = paddingValues.calculateTopPadding() + 10.dp,
            start = 10.dp,
            end = 10.dp
        )){

        val (searches,playlist,categorizer,button,searchadder,options, playlists) = createRefs()

        //searches
        if (isSearchlist.value){
            Row (modifier = Modifier
                .fillMaxWidth()
                .constrainAs(searches) {
                    top.linkTo(
                        parent.top,
                        margin = 5.dp
                    )
                },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){


                LazyRow (
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically){

                    items(searcheslist.value){search ->

                        customButton(text = search, modifier = Modifier) {
                            currentSearch.value = search
                            showSearchEditor.value = true
                        }

                    }
                }

                Icon(
                    modifier = Modifier.size(iconSize.dp)
                        .background(color = selected_button,
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            currentSearch.value = ""
                            showSearchEditor.value = true
                        },
                    imageVector = Icons.Default.Add,
                    contentDescription = "add search")

            }

        }


        //playlist songs
        Songlist(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(playlist) {
                if (isSearchlist.value) {
                    top.linkTo(
                        searches.bottom,
                        margin = 10.dp
                    )

                    bottom.linkTo(
                        categorizer.top,
                        margin = 10.dp
                    )
                } else {
                    top.linkTo(
                        parent.top,
                        margin = 10.dp
                    )

                    bottom.linkTo(
                        categorizer.top,
                        margin = 10.dp
                    )
                }

            },
            songlist = playList.value,
            isplaylist = true,
            onShowOptions = {isPlaylist ->
                showOptions.value = true
                isplaylist.value = isPlaylist
            }){
            navController.navigate(Routes.CurrentSongScreen(""))
        }

        //categorizer
        categorizer(
            selectedCategory = selectedCategory.value,
            modifier = Modifier
                .clip(RoundedCornerShape(16))
                .constrainAs(categorizer) {
                    bottom.linkTo(
                        button.top,
                        margin = 10.dp
                    )

                    centerHorizontallyTo(parent)
                }
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(5.dp)
        ) { letter ->
            selectedCategory.value = letter
        }

        //button
        customButton(
            "Post Top 3", modifier = Modifier
                .constrainAs(button) {
                    bottom.linkTo(
                        parent.bottom,
                        margin = 60.dp
                    )
                    end.linkTo(parent.end)
                }
                .padding(10.dp)
        ) {
            navController.navigate(Routes.Top3PostDetailsScreen(""))
        }


        //edit search
        if (showSearchEditor.value) {
            searchEditor(modifier = Modifier
                .constrainAs(searchadder) {
                    centerTo(parent)
                }
                .padding(10.dp),
                search = currentSearch.value,
                onRemoveEditor = {
                    showSearchEditor.value = false
                }
                , onDeleteSearch = {
                    showSearchEditor.value = false
            }, onChangeSearch = {
                    showSearchEditor.value = false
            }, onAddSearch = {
                    showSearchEditor.value = false
            })
        }

        //options
        if (showOptions.value){
            options(modifier = Modifier.constrainAs(options){
                centerTo(parent)
            },
                onShowPlaylist = { showPlaylist.value = true },
                onDeleteFromPlaylist = {
                    showOptions.value = false
                },
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
fun searchEditor(modifier: Modifier,
                search: String,
                 onRemoveEditor : () -> Unit,
                onDeleteSearch : () -> Unit,
                onChangeSearch : () -> Unit,
                 onAddSearch : () -> Unit){
    val newSearch = rememberSaveable {
        mutableStateOf(search)
    }


    Box(modifier = modifier) {
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(
                focusable = true
            ),
            onDismissRequest = {
                onRemoveEditor.invoke()
            }) {

            Column(
                Modifier
                    .clip(RoundedCornerShape(16))
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                TextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16)),
                    value = newSearch.value,
                    singleLine = true,
                    onValueChange = { newText ->
                        newSearch.value = newText
                    }, placeholder = {
                        Text(
                            text = "New Title",
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

                if (!search.isEmpty()){
                    Row(modifier = Modifier
                        , horizontalArrangement = Arrangement.spacedBy(30.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        customButton(text = "Edit", modifier = Modifier) {
                            onChangeSearch.invoke()
                        }

                        customButton(text = "Delete", modifier = Modifier) {
                            onDeleteSearch.invoke()
                        }

                    }
                }else{
                    Row(modifier = Modifier
                        , horizontalArrangement = Arrangement.spacedBy(30.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        customButton(text = "Add", modifier = Modifier) {
                            onAddSearch.invoke()
                        }

                    }
                }



            }

        }
    }


}