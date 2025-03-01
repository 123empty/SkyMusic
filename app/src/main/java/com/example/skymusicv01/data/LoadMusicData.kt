package com.example.skymusicv01.data

import android.content.Context
import android.util.Log
import com.example.skymusicv01.model.SongNote
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

// 读取 assets/music 目录中的所有 .txt 文件，返回 List<String> 类型
fun readAllTxtFromAssets(context: Context): List<String> {
    return try {
        // 获取 assets/music 目录中的所有 .txt 文件，并返回文件名列表
        context.assets.list("music")?.filter { it.endsWith(".txt") } ?: emptyList()
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
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
    //Log.d("TestJson", json)  // 打印日志查看解析的 JSON 数据

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