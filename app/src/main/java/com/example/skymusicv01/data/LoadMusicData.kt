package com.example.skymusicv01.data

import android.content.Context
import android.util.Log
import com.example.skymusicv01.model.SongNote
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

// 读取 assets/music 目录中的所有 .txt 文件，并解析为 List<Map<String, Any>> 类型
fun readAllTxtFromAssets(context: Context): List<List<Map<String, Any>>> {
    val songsList = mutableListOf<List<Map<String, Any>>>()

    try {
        // 获取 assets/music 目录中的所有文件
        val files = context.assets.list("music")?.filter { it.endsWith(".txt") }

        // 遍历所有的 .txt 文件
        files?.forEach { file ->
            try {
                // 读取并解析每个文件
                val songData = readTxtAndParseJsonFromAssets(context, "music/$file")
                songsList.add(songData) // 只在解析成功时添加到列表
            } catch (e: Exception) {
                // 如果某个文件不能正确解析，打印错误并跳过
                Log.e("TestJson", "Error parsing $file: ${e.message}")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return songsList
}

// 读取 txt 文件并解析为 JSON
fun readTxtAndParseJsonFromAssets(context: Context, fileName: String): List<Map<String, Any>> {
    // 从 assets/music 目录读取文件内容
    val inputStream = context.assets.open(fileName)
    val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-16"))
    val stringBuilder = StringBuilder()

    // 读取文件内容
    bufferedReader.forEachLine { stringBuilder.append(it) }

    // 将读取到的字符串解析为 JSON
    val json = stringBuilder.toString()
    Log.d("TestJson", json)  // 打印日志查看解析的 JSON 数据

    val gson = Gson()

    // 解析 JSON 数据为 List<Map<String, Any>>
    return try {
        val listType = object : TypeToken<List<Map<String, Any>>>() {}.type
        gson.fromJson(json, listType)
    } catch (e: Exception) {
        // 如果 JSON 解析失败，抛出异常
        throw Exception("Invalid JSON in file: $fileName")
    }
}

// 字符串转song notes
fun stringToSongNoteList(jsonString: String): List<SongNote> {
    val gson = Gson()

    // 解析为 List<SongNote>
    val listType = object : TypeToken<List<SongNote>>() {}.type
    return gson.fromJson(jsonString, listType)
}