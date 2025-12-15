package com.stevens.software.vibeplayer

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object PermissionScreen

@Serializable
object VibePlayer

@Composable
fun MainNavController() {
    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = PermissionScreen) {
        composable<PermissionScreen> {
            PermissionScreen(
                onNavigateToVibePlayer = { navController.navigate(VibePlayer) }
            )
        }
        composable<VibePlayer> {
            VibePlayerScreen()
        }
    }
}