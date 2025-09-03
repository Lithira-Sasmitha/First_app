package com.example.firstapp.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Bottom navigation items
sealed class BottomNavItem(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Filled.Home, "Home")
    object Users : BottomNavItem("users", Icons.Filled.AccountBox, "Users")
    object Profile : BottomNavItem("profile", Icons.Filled.Person, "Profile")
    object Settings : BottomNavItem("settings", Icons.Filled.Settings, "Settings")
}

@Composable
fun HomeScreenWithBottomNav() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Users,
        BottomNavItem.Profile,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, bottomNavItems) }
    ) { padding ->
        HomeNavHost(navController = navController, modifier = Modifier.padding(padding))
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<BottomNavItem>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun HomeNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = BottomNavItem.Home.route, modifier = modifier) {
        composable(BottomNavItem.Home.route) { HomePage() }
        composable(BottomNavItem.Users.route) { UsersListPage() }
        composable(BottomNavItem.Profile.route) { ProfilePage() }
        composable(BottomNavItem.Settings.route) { SettingsPage() }
    }
}

// Screens for each tab
@Composable
fun HomePage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home liuthi", fontSize = 28.sp, style = MaterialTheme.typography.headlineMedium)
    }
}



@Composable
fun ProfilePage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile Page", fontSize = 28.sp, style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun SettingsPage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Settings Page", fontSize = 28.sp, style = MaterialTheme.typography.headlineMedium)
    }
}
