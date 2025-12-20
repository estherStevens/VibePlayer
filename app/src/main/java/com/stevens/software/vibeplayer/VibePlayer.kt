package com.stevens.software.vibeplayer

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.vibeplayer.ui.common.Scanner
import com.stevens.software.vibeplayer.ui.common.TrackItem
import com.stevens.software.vibeplayer.ui.theme.PrimaryButton
import com.stevens.software.vibeplayer.ui.theme.extendedColours
import kotlinx.coroutines.launch

@Composable
fun VibePlayerScreen(
    viewModel: VibePlayerViewModel,
    onNavigateToPlayer: (String) -> Unit,
    onNavigateToScanMusic: () -> Unit,
    onNavigateToSearchScreen: () -> Unit
    ) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when(event) {
                is VibePlayerNavigationEvents.NavigateToPlayer -> {
                    onNavigateToPlayer(event.id)
                }
                VibePlayerNavigationEvents.NavigateToScanMusic -> {
                    onNavigateToScanMusic()
                }
                VibePlayerNavigationEvents.NavigateToSearch -> {
                    onNavigateToSearchScreen()
                }
            }
        }
    }

    VibePlayerView(
        uiState = uiState.value,
        onNavigateToPlayer = viewModel::onNavigateToPlayer,
        onNavigateToScanMusic = viewModel::onNavigateToScanMusic,
        onNavigateToSearchScreen = viewModel::onNavigateToSearchScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VibePlayerView(uiState: VibePlayerState,
                   onNavigateToPlayer: (String) -> Unit,
                   onNavigateToScanMusic: () -> Unit,
                   onNavigateToSearchScreen: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColours.bg)
    ) {
        Column {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = MaterialTheme.extendedColours.accent,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                actions = {
                    Icon(
                        painter = painterResource(R.drawable.scan_icon),
                        contentDescription = stringResource(R.string.scan_for_music),
                        tint = Color.Unspecified,
                        modifier = Modifier.clickable{
                            onNavigateToScanMusic()
                        }
                    )
                    Icon(
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = stringResource(R.string.search),
                        tint = Color.Unspecified,
                        modifier = Modifier.clickable{
                            onNavigateToSearchScreen()
                        }
                    )
                },
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.logo_small),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.extendedColours.bg
                )
            )
            when(uiState) {
                VibePlayerState.Empty -> EmptyState()
                VibePlayerState.Scanning -> ScannerState()
                is VibePlayerState.Tracks -> {
                    TracksState(
                        tracks = uiState.tracks,
                        onNavigateToPlayer = onNavigateToPlayer
                    )
                }
            }
        }

    }
}

@Composable
private fun EmptyState(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.empty_state_title),
            color = MaterialTheme.extendedColours.textPrimary,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.size(4.dp))
        Text(
            text = stringResource(R.string.empty_state_subtitle),
            color = MaterialTheme.extendedColours.textPrimary,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.size(20.dp))
        PrimaryButton(
            buttonText = R.string.empty_state_scan_again,
            onClick = {}
        )
    }
}

@Composable
private fun ScannerState(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Scanner(startAnimation = true)
        Spacer(Modifier.size(20.dp))
        Text(
            text = stringResource(R.string.scanner_scanning_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.extendedColours.textSecondary
        )
    }

}

@Composable
private fun TracksState(tracks: List<MediaItemUi>,
                        onNavigateToPlayer: (String) -> Unit){
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var showButton by remember { mutableStateOf(false) }
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemScrollOffset > 0
        }.collect { scrolled ->
            showButton = scrolled
        }
    }

    Box {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = listState
        ) {
            items(tracks) {
                TrackItem(
                    id = it.id,
                    albumArt = it.albumArt,
                    artist = it.artist,
                    trackTitle = it.title,
                    duration = it.duration,
                    onNavigateToPlayer = onNavigateToPlayer
                )
            }
        }
        if(showButton) {
            FloatingActionButton(
                shape = CircleShape,
                containerColor = MaterialTheme.extendedColours.buttonPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 12.dp)
                ,
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_up),
                    contentDescription = stringResource(R.string.scroll_to_top),
                    tint = Color.Unspecified
                )
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
private fun EmptyStateView() {
    VibePlayerView(
        VibePlayerState.Empty,
        onNavigateToPlayer = {},
        onNavigateToScanMusic = {},
        onNavigateToSearchScreen = {}
    )
}

@Preview(showSystemUi = true)
@Composable
private fun TracksView() {
    VibePlayerView(
        VibePlayerState.Tracks(
            listOf(
                MediaItemUi(
                    id = "1",
                    title = "really really really really  really long title",
                    duration = "3:00",
                    albumArt = Uri.EMPTY,
                    artist = "Arctic Monkeys"
                )
            )
        ),
        onNavigateToPlayer = {},
        onNavigateToScanMusic = {},
        onNavigateToSearchScreen = {},
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ScannerView() {
    VibePlayerView(
        VibePlayerState.Scanning,
        onNavigateToPlayer = {},
        onNavigateToScanMusic = {},
        onNavigateToSearchScreen = {}
    )
}