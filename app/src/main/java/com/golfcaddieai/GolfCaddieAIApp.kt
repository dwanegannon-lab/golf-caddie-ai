package com.golfcaddieai

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.golfcaddieai.ui.ClubBagScreen
import com.golfcaddieai.ui.HomeScreen
import com.golfcaddieai.ui.LoginScreen
import com.golfcaddieai.ui.RoundScreen
import com.golfcaddieai.viewmodel.GolfViewModel

@Composable
fun GolfCaddieAIApp(viewModel: GolfViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable("clubs") {
            ClubBagScreen(navController = navController, viewModel = viewModel)
        }
        composable("round") {
            RoundScreen(navController = navController, viewModel = viewModel)
        }
    }
}
