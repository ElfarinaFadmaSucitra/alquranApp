package com.example.alquran.data

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // mengambil semua surah
    @GET("surah")
    suspend fun getAllSurah(): SurahResponse

    // mengambil ayat dalam bahasa arab
    @GET("surah/{surahId}")
    suspend fun getAyatArabBySurah(
        @Path("surahId") surahId: Int
    ): AyatResponse

    // mengambil ayat dari satu surah, dengan terjemahan Indonesia
    @GET("surah/{surahId}/id.indonesian")
    suspend fun getAyatBySurah(
        @Path("surahId") surahId: Int
    ): AyatResponse

    @GET("surah/{id}/ar.alafasy")
    suspend fun getAudioSurah(
        @Path("id") id: Int
    ): AudioResponse

}
