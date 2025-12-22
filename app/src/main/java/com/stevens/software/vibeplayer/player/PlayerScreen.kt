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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.stevens.software.vibeplayer.media.RepeatMode
import com.stevens.software.vibeplayer.ui.theme.extendedColours

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    onBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect {
            when(it) {
                PlayerNavigationEvents.NavigateBack -> onBack()
            }
        }
    }
    PlayerView(
        isPlaying = uiState.value.isPlaying,
        title = uiState.value.title,
        artist = uiState.value.artist,
        artworkUri = uiState.value.artworkUri,
        currentPosition = uiState.value.currentPosition,
        duration = uiState.value.duration,
        isShuffleModeEnabled = uiState.value.isShuffleEnabled,
        repeatMode = uiState.value.repeatMode,
        onBack = { viewModel.onBack() },
        onPause = { viewModel.pause() },
        onResume = { viewModel.resume() },
        onSkipToNextTrack = { viewModel.onSkipToNextTrack() },
        onSkipToPreviousTrack = { viewModel.onSkipToPreviousTrack() },
        onSeek = { viewModel.onSeek(it) },
        onEnableShuffle = viewModel::onEnableShuffle,
        onEnableRepeatMode = viewModel::onRepeatModeChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerView(
    currentPosition: Long,
    duration: Long,
    isPlaying: Boolean,
    title: String,
    artist: String,
    artworkUri: Uri,
    isShuffleModeEnabled: Boolean,
    repeatMode: RepeatMode,
    onBack: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSkipToNextTrack: () -> Unit,
    onSkipToPreviousTrack: () -> Unit,
    onSeek: (Long) -> Unit,
    onEnableShuffle: (Boolean) -> Unit,
    onEnableRepeatMode: (RepeatMode) -> Unit
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
                        painter = painterResource(R.drawable.arrow_down),
                        contentDescription = stringResource(R.string.back),
                        tint = Color.Unspecified,
                        modifier = Modifier.clickable{
                            onBack()
                        }
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
                currentPosition = currentPosition,
                duration = duration,
                isPlaying = isPlaying,
                isShuffleModeEnabled = isShuffleModeEnabled,
                repeatMode = repeatMode,
                onPause = onPause,
                onResume = onResume,
                onSkipToNextTrack = onSkipToNextTrack,
                onSkipToPreviousTrack = onSkipToPreviousTrack,
                onSeek = onSeek,
                onEnableShuffle = onEnableShuffle,
                onEnableRepeatMode = onEnableRepeatMode
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
    currentPosition: Long,
    duration: Long,
    isPlaying: Boolean,
    isShuffleModeEnabled: Boolean,
    repeatMode: RepeatMode,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSeek: (Long) -> Unit,
    onSkipToNextTrack: () -> Unit,
    onSkipToPreviousTrack: () -> Unit,
    onEnableShuffle: (Boolean) -> Unit,
    onEnableRepeatMode: (RepeatMode) -> Unit
){
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {

        SeekBar(
            currentPosition = currentPosition,
            duration = duration,
            onSeek = onSeek
        )
        Spacer(Modifier.size(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(bottom = 30.dp)
                .fillMaxWidth()
        ) {
            Repeat(
                repeatMode = repeatMode,
                onEnableRepeatMode = onEnableRepeatMode
            )
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.player_back),
                contentDescription = stringResource(R.string.previous_track),
                tint = Color.Unspecified,
                modifier = Modifier.clickable {
                    onSkipToPreviousTrack()
                }
            )
            Spacer(Modifier.size(12.dp))
            if (isPlaying) {
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
                    modifier = Modifier.clickable {
                        onResume()
                    }
                )
            }
            Spacer(Modifier.size(12.dp))
            Icon(
                painter = painterResource(R.drawable.player_skip),
                contentDescription = stringResource(R.string.next_track),
                tint = Color.Unspecified,
                modifier = Modifier.clickable {
                    onSkipToNextTrack()
                }
            )
            Spacer(Modifier.weight(1f))
            Shuffle(
                isShuffleModeEnabled = isShuffleModeEnabled,
                onEnableShuffle = onEnableShuffle
            )
        }
    }
}

@Composable
private fun Repeat(
    repeatMode: RepeatMode,
    onEnableRepeatMode: (RepeatMode) -> Unit
){
    val image = when(repeatMode) {
        RepeatMode.REPEAT_ALL -> painterResource(R.drawable.player_repeat_all_enabled)
        RepeatMode.REPEAT_ONE -> painterResource(R.drawable.player_repeat_one_enabled)
        RepeatMode.REPEAT_OFF -> painterResource(R.drawable.repeat_all_disabled)
    }
    Icon(
        painter = image,
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier.clickable {
            onEnableRepeatMode(repeatMode)
        }
    )
}

@Composable
private fun Shuffle(
    isShuffleModeEnabled: Boolean,
    onEnableShuffle: (Boolean) -> Unit
){
    val image = when(isShuffleModeEnabled) {
        true -> painterResource(R.drawable.player_shuffle_enabled)
        false -> painterResource(R.drawable.player_shuffle_disabled)
    }
    val contentDescription = when(isShuffleModeEnabled) {
        true -> stringResource(R.string.disable_shuffle)
        false -> stringResource(R.string.enable_shuffle)
    }
    Icon(
        painter = image,
        contentDescription = contentDescription,
        tint = Color.Unspecified,
        modifier = Modifier.clickable {
            onEnableShuffle(isShuffleModeEnabled.not())
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeekBar(
    currentPosition: Long,
    duration: Long,
    onSeek: (Long) -> Unit
){
    val progress = if (duration > 0L) {
        currentPosition / duration.toFloat()
    } else 0f //todo move to uistate

    Slider(
        value = progress,
        onValueChangeFinished = {},
        onValueChange = { fraction ->
            onSeek((fraction * duration).toLong())
        },
        modifier = Modifier
            .fillMaxWidth(),

        colors = SliderDefaults.colors().copy(
            activeTrackColor = MaterialTheme.extendedColours.textPrimary,
            inactiveTrackColor = MaterialTheme.extendedColours.outline,
            thumbColor = Color.Transparent,
        ),
        thumb = {},
        track = { sliderState ->
            SliderDefaults.Track(
                modifier = Modifier.height(6.dp),
                sliderState = sliderState,
                thumbTrackGapSize = 0.dp,
                drawStopIndicator = null,
                colors = SliderDefaults.colors().copy(
                    activeTrackColor = MaterialTheme.extendedColours.textPrimary,
                    inactiveTrackColor = MaterialTheme.extendedColours.outline
                )
            )
        }
    )
}


@Preview(showSystemUi = true)
@Composable
private fun PlayerViewPreview(){
    PlayerView(
        isPlaying = false,
        title = "505",
        artist = "Arctic Monkeys",
        artworkUri = Uri.EMPTY,
        isShuffleModeEnabled = false,
        repeatMode = RepeatMode.REPEAT_OFF,
        currentPosition = 0,
        duration = 0,
        onBack = {},
        onPause = {},
        onResume = {},
        onSkipToNextTrack = {},
        onSkipToPreviousTrack = {},
        onSeek = {},
        onEnableShuffle = {},
        onEnableRepeatMode = {}
    )
}
