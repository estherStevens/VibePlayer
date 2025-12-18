package com.stevens.software.vibeplayer

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.stevens.software.vibeplayer.player.PlayerScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
object PermissionScreen

@Serializable
object VibePlayer

@Serializable
data class Player(val id: String)

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
                viewModel = koinViewModel(),
                onNavigateToPlayer = {
                    navController.navigate(Player(it))
                }
            )
        }
        composable<Player> { backStackEntry ->
            val routeArgs = backStackEntry.toRoute<Player>()
            PlayerScreen(
                viewModel = koinViewModel(
                    parameters = {
                        parametersOf(
                            routeArgs.id
                        )
                    }
                )
            )
        }
    }
}