package com.example.alquran.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquran.data.RetrofitClient
import com.example.alquran.data.Surah
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SurahViewModel : ViewModel() {
    private val _surahList = MutableStateFlow<List<Surah>>(emptyList())
    val surahList: StateFlow<List<Surah>> = _surahList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _lastReadSurah = MutableStateFlow<Surah?>(null)
    val lastReadSurah: StateFlow<Surah?> = _lastReadSurah

    fun setLastReadSurah(surah: Surah) {
        _lastReadSurah.value = surah
    }
    init {
        getSurahList()
    }
    private fun getSurahList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.apiService.getAllSurah()
                _surahList.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}