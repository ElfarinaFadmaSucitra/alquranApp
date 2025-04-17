package com.example.alquran.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquran.data.CombinedAyat
import com.example.alquran.data.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val _ayahList = MutableStateFlow<List<CombinedAyat>>(emptyList())
    val ayahList: StateFlow<List<CombinedAyat>> = _ayahList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _surahName = MutableStateFlow("")
    val surahName: StateFlow<String> = _surahName

    private val _juz = MutableStateFlow<Int?>(null)
    val juz: StateFlow<Int?> = _juz

    fun fetchAyatBySurah(surahId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val responseArab = RetrofitClient.apiService.getAyatArabBySurah(surahId)
                val responseIndo = RetrofitClient.apiService.getAyatBySurah(surahId)
                val audioResponse = RetrofitClient.apiService.getAudioSurah(surahId)

                val arabAyatList = responseArab.data.ayahs
                val indoAyatList = responseIndo.data.ayahs
                val audioAyatList = audioResponse.data.ayahs

                _surahName.value = responseArab.data.name

                if (arabAyatList.isNotEmpty()) {
                    _juz.value = arabAyatList.first().juz
                }

                val combined = arabAyatList.mapIndexedNotNull { index, arabAyat ->
                    val indoAyat = indoAyatList.find { it.numberInSurah == arabAyat.numberInSurah }
                    val audioAyat = audioAyatList.getOrNull(index)
                    if (indoAyat != null && audioAyat?.audio != null) {
                        CombinedAyat(
                            numberInSurah = arabAyat.numberInSurah,
                            arabText = arabAyat.text,
                            translation = indoAyat.text,
                            audioUrl = audioAyat.audio
                        )
                    } else null
                }
                _ayahList.value = combined
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
