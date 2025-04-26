package com.example.blaze

sealed class Screen(val route: String, val title: String) {
    object Tasks : Screen("tasks", "Tasks")
    object Health : Screen("health", "Health")
    object Social : Screen("social", "Social")
}

