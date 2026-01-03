package com.stevens.software.vibeplayer

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stevens.software.vibeplayer.player.MinimisedPlayerView
import com.stevens.software.vibeplayer.player.MinimisedPlayerViewModel
import com.stevens.software.vibeplayer.ui.common.Scanner
import com.stevens.software.vibeplayer.ui.common.SecondaryButton
import com.stevens.software.vibeplayer.ui.common.TrackItem
import com.stevens.software.vibeplayer.ui.theme.PrimaryButton
import com.stevens.software.vibeplayer.ui.theme.extendedColours
import kotlinx.coroutines.launch

@Composable
fun VibePlayerScreen(
    viewModel: VibePlayerViewModel,
    minimisedPlayerViewModel: MinimisedPlayerViewModel,
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
        minimisedPlayerViewModel = minimisedPlayerViewModel,
        onNavigateToPlayer = viewModel::onNavigateToPlayer,
        onNavigateToScanMusic = viewModel::onNavigateToScanMusic,
        onNavigateToSearchScreen = viewModel::onNavigateToSearchScreen,
        onPlayFromBeginning = viewModel::onPlayFromBeginning,
        onEnableShuffle = viewModel::onEnableShuffle
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VibePlayerView(uiState: VibePlayerState,
                   minimisedPlayerViewModel: MinimisedPlayerViewModel,
                   onNavigateToPlayer: (String) -> Unit,
                   onNavigateToScanMusic: () -> Unit,
                   onNavigateToSearchScreen: () -> Unit,
                   onEnableShuffle: () -> Unit,
                   onPlayFromBeginning: () -> Unit
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
                        onNavigateToPlayer = onNavigateToPlayer,
                        onEnableShuffle = onEnableShuffle,
                        onPlayFromBeginning = onPlayFromBeginning
                    )
                }
            }
        }

        MinimisedPlayerView(
            viewModel = minimisedPlayerViewModel,
            modifier = Modifier.align(
                Alignment.BottomStart
            )
        )
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
                        onNavigateToPlayer: (String) -> Unit,
                        onEnableShuffle: () -> Unit,
                        onPlayFromBeginning: () -> Unit){
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
            state = listState,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ShuffleButton(
                        onEnableShuffle = onEnableShuffle
                    )
                    PlayAllButton(
                        onPlayAllClicked = onPlayFromBeginning
                    )
                }
                Spacer(Modifier.size(8.dp))
            }
            item {
                Text(
                    text = stringResource(R.string.number_of_tracks, tracks.size),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.extendedColours.textSecondary
                )
                Spacer(Modifier.size(8.dp))
            }
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

@Composable
private fun RowScope.ShuffleButton(
    onEnableShuffle: () -> Unit
){
    SecondaryButton(
        buttonText = R.string.shuffle,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.shuffle),
                contentDescription = null,
                tint = Color.Unspecified
            )
        },
        modifier = Modifier.weight(1f),
        onClick = onEnableShuffle
    )
}


@Composable
private fun RowScope.PlayAllButton(
    onPlayAllClicked: () -> Unit
){
    SecondaryButton(
        buttonText = R.string.play_all,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.play_all),
                contentDescription = null,
                tint = Color.Unspecified
            )
        },
        modifier = Modifier.weight(1f),
        onClick = onPlayAllClicked
    )
}

//@Preview(showSystemUi = true)
//@Composable
//private fun EmptyStateView() {
//    VibePlayerView(
//        VibePlayerState.Empty,
//        onNavigateToPlayer = {},
//        onNavigateToScanMusic = {},
//        onNavigateToSearchScreen = {},
//        onEnableShuffle = {},
//        onPlayFromBeginning = {}
//    )
//}
//
//@Preview(showSystemUi = true)
//@Composable
//private fun TracksView() {
//    VibePlayerView(
//        VibePlayerState.Tracks(
//            listOf(
//                MediaItemUi(
//                    id = "1",
//                    title = "really really really really  really long title",
//                    duration = "3:00",
//                    albumArt = Uri.EMPTY,
//                    artist = "Arctic Monkeys"
//                )
//            )
//        ),
//        onNavigateToPlayer = {},
//        onNavigateToScanMusic = {},
//        onNavigateToSearchScreen = {},
//        onEnableShuffle = {},
//        onPlayFromBeginning = {}
//    )
//}
//
//@Preview(showSystemUi = true)
//@Composable
//private fun ScannerView() {
//    VibePlayerView(
//        VibePlayerState.Scanning,
//        onNavigateToPlayer = {},
//        onNavigateToScanMusic = {},
//        onNavigateToSearchScreen = {},
//        onEnableShuffle = {},
//        onPlayFromBeginning = {}
//    )
//}