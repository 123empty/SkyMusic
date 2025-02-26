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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skymusicv01.FxManager.context
import com.example.skymusicv01.data.readAllTxtFromAssets
import com.example.skymusicv01.data.stringToSongNoteList
import com.example.skymusicv01.model.Song
import com.example.skymusicv01.model.SongNote

@Composable
fun SearchMusicFloating(
    backClick:()->Unit={},
    itemClick:(Song)->Unit={}
) {
    var searchText by remember { mutableStateOf("") }

    // 使用 remember 缓存读取的歌曲数据，避免每次重组时重新加载
    val allSongs = remember { mutableStateOf<List<List<Map<String, Any>>>>(emptyList()) }

    // 只在首次组合时读取数据
    LaunchedEffect(Unit) {
        // 读取并解析所有txt文件
        val songs = readAllTxtFromAssets(context)
        allSongs.value = songs // 设置状态
    }

    val filteredList = allSongs.value.flatten().filter { song ->
        val name = song["name"] as? String
        name?.contains(searchText, ignoreCase = true) == true
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
            items(filteredList) { song ->
                val name = song["name"] as? String ?: "Unknown Song"
                val author = (song["author"] as? String).takeIf { it?.isNotEmpty() == true } ?: "Unknown Author"
                Row(
                    modifier = Modifier.clickable {
                        val songNotes:List<SongNote> = stringToSongNoteList(song["songNotes"].toString())
                        val transcribedBy = (song["transcribedBy"] as? String).takeIf { it?.isNotEmpty() == true } ?: "Unknown TranscribedBy"
                        //Log.d("TestFloating","${Song(name,author,transcribedBy,songNotes)}")
                        itemClick(Song(name,author,transcribedBy,songNotes))
                    }
                ){
                    Text(
                        text = "$name-$author",
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