package com.stevens.software.vibeplayer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.vibeplayer.ui.theme.PrimaryButton
import com.stevens.software.vibeplayer.ui.theme.extendedColours

@Composable
fun VibePlayerScreen(viewModel: VibePlayerViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    VibePlayerView(uiState.value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VibePlayerView(uiState: VibePlayerState) {
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
                VibePlayerState.Tracks -> TracksState()
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
        Scanner()
        Spacer(Modifier.size(20.dp))
        Text(
            text = stringResource(R.string.scanner_scanning_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.extendedColours.textSecondary
        )
    }

}

@Composable
private fun Scanner(){
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    Image(
        painter = painterResource(R.drawable.scanner),
        contentDescription = stringResource(R.string.scanner_scanning),
        modifier = Modifier.rotate(rotation)
    )
}

@Composable
private fun TracksState(){
    TrackItem()
}

@Composable
private fun TrackItem(){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {

        }
        Spacer(Modifier.size(12.dp))
        Column {
            Text(
                text = "505",
                color = MaterialTheme.extendedColours.textPrimary,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.size(2.dp))
            Text(
                text = "Arctic Monkeys",
                color = MaterialTheme.extendedColours.textSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = "4:14",
            color = MaterialTheme.extendedColours.textSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun EmptyStateView(){
    VibePlayerView(VibePlayerState.Empty)
}

@Preview(showSystemUi = true)
@Composable
private fun TracksView(){
    VibePlayerView(VibePlayerState.Tracks)
}

@Preview(showSystemUi = true)
@Composable
private fun ScannerView(){
    VibePlayerView(VibePlayerState.Scanning)
}