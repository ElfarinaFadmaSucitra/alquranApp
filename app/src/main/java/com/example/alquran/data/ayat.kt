package com.example.alquran.data

import com.google.gson.annotations.SerializedName

data class AyatResponse(
    val code: Int,
    val status: String,
    val data: SurahDetail
)

data class SurahDetail(
    val number: Int,
    val name: String,
    @SerializedName("ayahs") val ayahs: List<Ayat>
)

data class Ayat(
    val number: Int,
    val text: String,
    val numberInSurah: Int,
    val juz: Int,

    @SerializedName("audio")
    val audio: String? = null
)