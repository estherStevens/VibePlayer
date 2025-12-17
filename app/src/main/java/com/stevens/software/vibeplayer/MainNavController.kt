package com.stevens.software.vibeplayer

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stevens.software.vibeplayer.player.PlayerScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object PermissionScreen

@Serializable
object VibePlayer

@Serializable
object Player

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
            VibePlayerScreen(
                viewModel = koinViewModel()
            )
        }
        composable<Player> {
            PlayerScreen(
//                viewModel = koinViewModel()
            )
        }
    }
}