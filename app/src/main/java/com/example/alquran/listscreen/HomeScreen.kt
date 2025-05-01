package com.example.alquran.listscreen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alquran.R
import com.example.alquran.firebase.GoogleAuth
import com.example.alquran.viewmodel.SurahViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    surahViewModel: SurahViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity
    val webClientId = "978164828193-sark0kud0l4mm82c81plms4fkvm3vqku.apps.googleusercontent.com" // webClientId dari Firebase

    val googleSignInClient = remember {
        GoogleAuth.getGoogleSignInClient(context, webClientId)
    }

    val launcher = rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if (task.isSuccessful) {
            val account = task.result
            GoogleAuth.firebaseAuthWithGoogle(account, context,
                onSuccess = {
                    Toast.makeText(context, "Login Google berhasil", Toast.LENGTH_SHORT).show()
                    navController.navigate("surah_list")
                },
                onError = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            Toast.makeText(context, "Login Google gagal", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Al-Qur'an", color = Color.White, style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF388E3C)),
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).background(Color(0xFFFFF7F0)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.quran),
                contentDescription = "Logo Al-Qur'an",
                modifier = Modifier.size(250.dp).padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    googleSignInClient.signOut().addOnCompleteListener {
                        val signInIntent = googleSignInClient.signInIntent
                        launcher.launch(signInIntent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(MaterialTheme.shapes.medium),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                elevation = ButtonDefaults.buttonElevation(5.dp),
            ) {
                Text("Login dengan Google", color = Color.White, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
