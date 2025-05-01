package com.example.alquran.navigasi

import SurahListScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.alquran.listscreen.DetailSurahScreen
import com.example.alquran.listscreen.HomeScreen
import com.example.alquran.viewmodel.DetailViewModel
import com.example.alquran.viewmodel.SurahViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun QuranNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    val auth = FirebaseAuth.getInstance()
    val user: FirebaseUser? = auth.currentUser // Mendapatkan user yang sedang login

    val onLogout: () -> Unit = {
        auth.signOut()
        navController.navigate("home")
    }

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            val viewModel: SurahViewModel = viewModel()
            HomeScreen(navController = navController, surahViewModel = viewModel)
        }
        composable("surah_list") {
            val viewModel: SurahViewModel = viewModel()
            SurahListScreen(
                navController = navController,
                viewModel = viewModel,
                user = user,
                onLogout = onLogout
            )
        }
        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 1
            val viewModel: DetailViewModel = viewModel()
            DetailSurahScreen(surahId = id, viewModel = viewModel, navController = navController)
        }
    }
}
