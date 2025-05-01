
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alquran.viewmodel.SurahViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfileDialog(
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser = remember { FirebaseAuth.getInstance().currentUser }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            Column(
                modifier = Modifier.background(Color.White).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentUser?.photoUrl != null) {
                    AsyncImage(
                        model = currentUser.photoUrl,
                        contentDescription = "User Photo",
                        modifier = Modifier.size(72.dp).clip(CircleShape).background(Color.LightGray)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(72.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentUser?.displayName ?: "Tidak diketahui",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = currentUser?.email ?: "Email tidak tersedia",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Tutup")
                    }
                    Button(
                        onClick = onLogout,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        Text("Logout", color = Color.White)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahListScreen(
    navController: NavController,
    viewModel: SurahViewModel,
    user: FirebaseUser?,
    onLogout: () -> Unit
) {
    val surahList = viewModel.surahList.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val lastReadSurah = viewModel.lastReadSurah.collectAsState().value

    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var showUserInfo by remember { mutableStateOf(false) }

    val currentUser = remember { FirebaseAuth.getInstance().currentUser }

    val filteredSurahList = if (searchText.text.isEmpty()) {
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
                    Text("Daftar Surah", color = Color.White)
                },
                actions = {
                    IconButton(onClick = { showUserInfo = true }) {
                        if (currentUser?.photoUrl != null) {
                            AsyncImage(
                                model = currentUser.photoUrl,
                                contentDescription = "User Info",
                                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Gray)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "User Info",
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF388E3C))
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Pencarian
                    Card(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(6.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        TextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Cari Surah...", color = Color.Gray) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon",
                                    tint = Color.Gray
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    // Daftar Surah
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().background(Color(0xFFFFF8E7))
                    ) {
                        items(filteredSurahList) { surah ->
                            Card(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp).fillMaxWidth().clickable {
                                        viewModel.setLastReadSurah(surah)
                                        navController.navigate("detail/${surah.number}")
                                    },
                                elevation = CardDefaults.cardElevation(4.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column {
                                            Text(
                                                text = "${surah.number}. ${surah.englishName}",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2E7D32)
                                            )
                                            Text(
                                                text = "(${surah.englishNameTranslation})",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = if (surah.revelationType == "Meccan") "Makkiyah" else "Madaniyyah",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.DarkGray
                                            )
                                            Text(
                                                text = "${surah.numberOfAyahs} Ayat",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.DarkGray
                                            )
                                        }
                                    }

                                    if (lastReadSurah?.number == surah.number) {
                                        Text(
                                            text = "Terakhir dibaca",
                                            color = Color(0xFF388E3C),
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Tampilkan dialog profil
            if (showUserInfo) {
                ProfileDialog(
                    onDismiss = { showUserInfo = false },
                    onLogout = {
                        onLogout()
                        showUserInfo = false
                    }
                )
            }
        }
    }
}
