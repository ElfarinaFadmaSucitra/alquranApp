package com.example.alquran.listscreen

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alquran.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSurahScreen(
    surahId: Int,
    viewModel: DetailViewModel,
    navController: NavController
) {
    val ayatList = viewModel.ayahList.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val surahName = viewModel.surahName.collectAsState().value
    val juz = viewModel.juz.collectAsState().value

    LaunchedEffect(surahId) {
        viewModel.fetchAyatBySurah(surahId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Detail Surah", style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Text(surahName, style = MaterialTheme.typography.titleLarge, color = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF388E3C)
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFFFF8E7))) {
                    items(ayatList) { ayat ->
                        Box(
                            modifier = Modifier.padding(8.dp).fillMaxWidth().background(
                                    color = if (ayat.numberInSurah % 2 == 0) {
                                        Color(0xFFE3F2FD) //  ayat genap
                                    } else {
                                        Color(0xFFFFF8E7) // ayat ganjil
                                    }
                                )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                if (ayat == ayatList.first() && juz != 0) {
                                    Text(
                                        "Juz $juz",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color(0xFF388E3C),
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                    Spacer(modifier = Modifier.height(22.dp))
                                }
                                Text(
                                    "${ayat.numberInSurah}.",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    ayat.arabText,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                                )
                                Text(ayat.translation, style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(8.dp))

                                // tombol audio
                                val mediaPlayer = remember { MediaPlayer() }
                                if (ayat.audioUrl.isNotBlank()) {
                                    Button(onClick = {
                                        try {
                                            mediaPlayer.reset()
                                            mediaPlayer.setDataSource(ayat.audioUrl)
                                            mediaPlayer.prepare()
                                            mediaPlayer.start()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366))
                                    ) {
                                        Text("Play", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

