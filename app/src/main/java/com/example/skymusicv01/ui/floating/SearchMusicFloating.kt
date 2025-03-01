package com.example.skymusicv01.ui.floating

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skymusicv01.data.readAllTxtFromAssets
import com.example.skymusicv01.data.readTxtAndParseJsonFromAssets
import com.example.skymusicv01.data.stringToSongNoteList
import com.example.skymusicv01.model.Song
import com.example.skymusicv01.model.SongNote

@Composable
fun SearchMusicFloating(
    backClick: () -> Unit = {},
    itemClick: (Song) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val allSongsFilesName = remember { mutableStateOf<List<String>>(emptyList()) }

    // 读取数据
    LaunchedEffect(Unit) {
        val songsFilesName = readAllTxtFromAssets(context) // 读取所有 txt 文件名
        allSongsFilesName.value = songsFilesName
    }

    val filteredList = allSongsFilesName.value.filter { songFileName ->
        songFileName.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        modifier = Modifier.size(250.dp, 300.dp),
        topBar = {
            Row{
                SearchBar(
                    modifier = Modifier.weight(1f),
                    searchText = searchText,
                    onSearchTextChange = { searchText = it }
                )
                IconButton(
                    onClick = backClick,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(filteredList) { songFileName ->
                Row(
                    modifier = Modifier.clickable {
                        val song = readTxtAndParseJsonFromAssets(context,"music/$songFileName")
                        val name = song[0]["name"] as? String ?: "Unknown Song"
                        val author = (song[0]["author"] as? String).takeIf { it?.isNotEmpty() == true } ?: "Unknown Author"
                        val songNotes:List<SongNote> = stringToSongNoteList(song[0]["songNotes"].toString())
                        val transcribedBy = (song[0]["transcribedBy"] as? String).takeIf { it?.isNotEmpty() == true } ?: "Unknown TranscribedBy"
                        //Log.d("TestFloating","${Song(name,author,transcribedBy,songNotes)}")
                        itemClick(Song(name,author,transcribedBy,songNotes))
                    }
                ){
                    Text(
                        text = songFileName.removeSuffix(".txt"),
                        maxLines = 1,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    TextField(
        value = searchText,
        onValueChange = { newText ->
            onSearchTextChange(newText)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text("搜索")
        },
        modifier = modifier
            .height(30.dp)
            .padding(horizontal = 5.dp)
    )
}



@Preview
@Composable
fun SearchMusicFloatingPreview(){
    SearchMusicFloating()
}