package com.stevens.software.vibeplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun VibePlayerView(uiState: VibePlayerState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColours.bg)
    ) {
        when(uiState) {
            VibePlayerState.Empty -> EmptyState()
            VibePlayerState.Scanning -> {}
            VibePlayerState.Tracks -> {}
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

@Preview(showSystemUi = true)
@Composable
private fun EmptyStateView(){
    EmptyState()
}