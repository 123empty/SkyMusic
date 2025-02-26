package com.example.skymusicv01.model

data class SongNote(
    val time: Long,
    val key: String
)

data class Song(
    val name: String,
    val author:String,
    val transcribedBy:String,
    val songNotes: List<SongNote>
)
