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
import androidx.compose.runtime.mutableStateOf
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
    val mediaPlayer = remember { MediaPlayer() }
    val currentPlayingAyat = remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(surahId) {
        viewModel.fetchAyatBySurah(surahId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Detail Surah",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Text(
                            surahName,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFF8E7))
                        .padding(horizontal = 8.dp)
                ) {
                    items(ayatList) { ayat ->
                        Card(
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (ayat.numberInSurah % 2 == 0)
                                    Color(0xFFE8F5E9)
                                else
                                    Color(0xFFFFF3E0)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                if (ayat == ayatList.first() && juz != 0) {
                                    Text(
                                        "Juz $juz",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFF1B5E20),
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(bottom = 8.dp)
                                    )
                                }

                                Text(
                                    "${ayat.numberInSurah}.",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    ayat.arabText,
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    ayat.translation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                if (ayat.audioUrl.isNotBlank()) {
                                    Button(
                                        onClick = {
                                            try {
                                                if (mediaPlayer.isPlaying) {
                                                    mediaPlayer.stop()
                                                    mediaPlayer.reset()
                                                }
                                                mediaPlayer.setDataSource(ayat.audioUrl)
                                                mediaPlayer.prepare()
                                                mediaPlayer.start()

                                                currentPlayingAyat.value = ayat.numberInSurah
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF2E7D32)
                                        )
                                    ) {
                                        Text(
                                            text = "Play",
                                            color = Color.White
                                        )
                                    }
                                    if (currentPlayingAyat.value == ayat.numberInSurah) {
                                        Button(
                                            onClick = {
                                                if (mediaPlayer.isPlaying) {
                                                    mediaPlayer.stop()
                                                    mediaPlayer.reset()
                                                }
                                                currentPlayingAyat.value = null
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF2E7D32)
                                            )
                                        ) {
                                            Text(
                                                text = "Stop",
                                                color = Color.White
                                            )
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
}


