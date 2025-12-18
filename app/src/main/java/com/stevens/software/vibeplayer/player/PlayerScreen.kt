package com.stevens.software.vibeplayer.player

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.stevens.software.vibeplayer.R
import com.stevens.software.vibeplayer.ui.theme.extendedColours

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    PlayerView(
        isPlaying = uiState.value.isPlaying,
        title = uiState.value.title,
        artist = uiState.value.artist,
        artworkUri = uiState.value.artworkUri,
        onPause = { viewModel.pause() },
        onResume = { viewModel.resume() },
        onSkipToNextTrack = { viewModel.onSkipToNextTrack() },
        onSkipToPreviousTrack = { viewModel.onSkipToPreviousTrack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerView(
    isPlaying: Boolean,
    title: String,
    artist: String,
    artworkUri: Uri,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSkipToNextTrack: () -> Unit,
    onSkipToPreviousTrack: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColours.bg)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

        ) {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.extendedColours.bg
                ),
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.back),
                        contentDescription = stringResource(R.string.back),
                        tint = Color.Unspecified
                    )
                }
            )
            Box(
                modifier = Modifier
                    .size(320.dp)
                    .clip(RoundedCornerShape(10.dp)),
            ){
                AsyncImage(
                    model = artworkUri,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(320.dp)
                )
            }
            Spacer(Modifier.size(24.dp))
            TrackTitle(title)
            Spacer(Modifier.size(4.dp))
            Artist(artist)
            Spacer(Modifier.weight(1f))
            TrackControls(
                isPlaying = isPlaying,
                onPause = onPause,
                onResume = onResume,
                onSkipToNextTrack = onSkipToNextTrack,
                onSkipToPreviousTrack = onSkipToPreviousTrack
            )
        }
    }
}

@Composable
private fun TrackTitle(title: String){
    Text(
        text = title,
        color = MaterialTheme.extendedColours.textPrimary,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun Artist(artist: String){
    Text(
        text = artist,
        color = MaterialTheme.extendedColours.textSecondary,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun TrackControls(
    isPlaying: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSkipToNextTrack: () -> Unit,
    onSkipToPreviousTrack: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically, 
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(bottom = 30.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.player_back),
            contentDescription = stringResource(R.string.previous_track),
            tint = Color.Unspecified,
            modifier = Modifier.clickable{
                onSkipToPreviousTrack()
            }
        )

        if(isPlaying) {
            Icon(
                painter = painterResource(R.drawable.player_pause),
                contentDescription = stringResource(R.string.play_track),
                tint = Color.Unspecified,
                modifier = Modifier.clickable {
                    onPause()
                }
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.player_play),
                contentDescription = stringResource(R.string.play_track),
                tint = Color.Unspecified,
                modifier = Modifier.clickable{
                    onResume()
                }
            )
        }

        Icon(
            painter = painterResource(R.drawable.player_skip),
            contentDescription = stringResource(R.string.next_track),
            tint = Color.Unspecified,
            modifier = Modifier.clickable{
                onSkipToNextTrack()
            }
        )
    }
}


@Preview(showSystemUi = true)
@Composable
private fun PlayerViewPreview(){
    PlayerView(
        isPlaying = false,
        title = "505",
        artist = "Arctic Monkeys",
        artworkUri = Uri.EMPTY,
        onPause = {},
        onResume = {},
        onSkipToNextTrack = {},
        onSkipToPreviousTrack = {}
    )
}
