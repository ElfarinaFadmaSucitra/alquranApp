package com.example.alquran.navigasi

import SurahListScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.alquran.listscreen.DetailSurahScreen
import com.example.alquran.viewmodel.DetailViewModel
import com.example.alquran.viewmodel.SurahViewModel

@Composable
fun QuranNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "surah_list") {
        composable("surah_list") {
            val viewModel: SurahViewModel = viewModel()
            SurahListScreen(navController = navController, viewModel = viewModel)
        }
        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 1
            val viewModel: DetailViewModel = viewModel()
            DetailSurahScreen(surahId = id, viewModel = viewModel, navController = navController) //  navController
        }
    }
}