package com.example.alquran.data

data class AudioResponse(
    val code: Int,
    val status: String,
    val data: SurahDetail
)