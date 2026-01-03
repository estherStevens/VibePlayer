package com.stevens.software.vibeplayer.player

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.stevens.software.vibeplayer.R
import com.stevens.software.vibeplayer.ui.theme.extendedColours

@Composable
fun MinimisedPlayerView(
     viewModel: MinimisedPlayerViewModel,
     modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    MinimisedPlayer(
        trackTitle = uiState.value.trackTitle,
        albumArt = uiState.value.albumArt,
        artist = uiState.value.artist,
        isPlaying = uiState.value.isPlaying,
        modifier = modifier,
        onPause = viewModel::onPause,
        onResume = viewModel::onResume,
        onSkipToNextTrack = viewModel::onSkipToNextTrack
    )
}

@Composable
private fun MinimisedPlayer(
    trackTitle: String,
    artist: String,
    albumArt: Uri,
    isPlaying: Boolean,
    modifier: Modifier,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSkipToNextTrack: () -> Unit
){
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.extendedColours.outline,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            )
            .height(112.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box( //todo make reusable?
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                AsyncImage(
                    model = albumArt,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp),
                    placeholder = painterResource(R.drawable.tracklist_image_placeholder)
                )
            }
            Spacer(Modifier.size(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = trackTitle,
                    color = MaterialTheme.extendedColours.textPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.size(2.dp))
                Text(
                    text = artist,
                    color = MaterialTheme.extendedColours.textSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (isPlaying) {
                Icon(
                    painter = painterResource(R.drawable.player_pause),
                    contentDescription = stringResource(R.string.play_track),
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(44.dp)
                        .clickable {
                            onPause()
                        }
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.player_play),
                    contentDescription = stringResource(R.string.play_track),
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(44.dp)
                        .clickable {
                            onResume()
                        }
                )
            }
            Spacer(Modifier.size(8.dp))
            Icon(
                painter = painterResource(R.drawable.player_skip),
                contentDescription = stringResource(R.string.next_track),
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(44.dp)
                    .clickable{
                        onSkipToNextTrack()
                    }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MinimisedPlayerPreview(){
    MaterialTheme {
        MinimisedPlayer(
            albumArt = Uri.EMPTY,
            trackTitle = "505",
            artist = "Arctic Monkeys",
            isPlaying = false,
            modifier = Modifier,
            onPause = {},
            onResume = {},
            onSkipToNextTrack = {}
        )
    }

}