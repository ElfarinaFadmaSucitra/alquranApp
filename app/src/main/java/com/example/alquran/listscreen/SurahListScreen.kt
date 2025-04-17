import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alquran.viewmodel.SurahViewModel
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahListScreen(navController: NavController, viewModel: SurahViewModel) {
    val surahList = viewModel.surahList.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val lastReadSurah = viewModel.lastReadSurah.collectAsState().value

    var searchMode by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val SurahList = if (searchText.text.isEmpty()) {
        surahList
    } else {
        surahList.filter {
            it.englishName.contains(searchText.text, ignoreCase = true) ||
                    it.englishNameTranslation.contains(searchText.text, ignoreCase = true)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (searchMode) {
                        TextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Cari Surah...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            "Daftar Surah",
                            color = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { searchMode = !searchMode }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors( containerColor = Color(0xFF388E3C)
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFFFF8E7))) {
                    items(SurahList) { surah ->
                        Card(
                            modifier = Modifier.padding(8.dp).fillMaxWidth().clickable {
                                    viewModel.setLastReadSurah(surah)
                                    navController.navigate("detail/${surah.number}")
                                },
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "${surah.number}. ${surah.englishName}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "(${surah.englishNameTranslation})",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Jenis: ${if (surah.revelationType == "Meccan") "Makkiyah" else "Madaniyyah"}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Jumlah Ayat: ${surah.numberOfAyahs}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                if (lastReadSurah?.number == surah.number) {
                                    Text(
                                        text = "Terakhir dibaca",
                                        color = Color(0xFF388E3C),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
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
