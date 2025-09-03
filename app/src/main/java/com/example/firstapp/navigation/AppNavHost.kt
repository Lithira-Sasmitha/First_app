package com.example.firstapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.firstapp.feature.splash.SplashScreen
import com.example.firstapp.feature.auth.LoginScreen
import com.example.firstapp.feature.home.HomeScreenWithBottomNav  // <- import correct HomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreenWithBottomNav() }  // <- use the correct screen
    }
}
