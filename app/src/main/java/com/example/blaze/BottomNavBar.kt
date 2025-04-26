package com.example.blaze

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(Screen.Tasks, Screen.Health, Screen.Social)
    NavigationBar {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(Icons.Default.Favorite, contentDescription = null) }, // use meaningful icons later
                label = { Text(screen.title) },
                selected = currentDestination == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
