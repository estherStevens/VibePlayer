package com.stevens.software.vibeplayer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.stevens.software.vibeplayer.player.PlayerScreen
import com.stevens.software.vibeplayer.scan.ScanScreen
import com.stevens.software.vibeplayer.search.SearchScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
object PermissionScreen

@Serializable
object VibePlayer

@Serializable
data class Player(val id: String)

@Serializable
object ScanMusic

@Serializable
object Search

@Composable
fun MainNavController(innerPadding: PaddingValues) {
    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = PermissionScreen) {
        composable<PermissionScreen> {
            PermissionScreen(
                paddingValues = innerPadding,
                onNavigateToVibePlayer = { navController.navigate(VibePlayer) }
            )
        }
        composable<VibePlayer> {
            VibePlayerScreen(
                viewModel = koinViewModel(),
                onNavigateToPlayer = {
                    navController.navigate(Player(it))
                },
                onNavigateToScanMusic = {
                    navController.navigate(ScanMusic)
                },
                onNavigateToSearchScreen = {
                    navController.navigate(Search)
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
                ),
                onBack = { navController.popBackStack() }
            )
        }
        composable<ScanMusic> {
            ScanScreen(
                viewModel = koinViewModel(),
                onBack = { navController.popBackStack() },
                onNavigateToTrackListing = { navController.popBackStack() }
            )
        }
        composable<Search> {
            SearchScreen(
                viewModel = koinViewModel(),
                paddingValues = innerPadding,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { navController.navigate(Player(it)) }
            )
        }
    }
}