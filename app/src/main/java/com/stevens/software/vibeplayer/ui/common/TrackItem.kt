package com.stevens.software.vibeplayer.ui.common

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.stevens.software.vibeplayer.R
import com.stevens.software.vibeplayer.ui.theme.extendedColours

@Composable
fun TrackItem(
    id: String,
    albumArt: Uri,
    artist: String,
    trackTitle: String,
    duration: String,
    onNavigateToPlayer: (String) -> Unit,
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                onNavigateToPlayer(id)
            }
    ) {
        Box(
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
        Spacer(Modifier.size(16.dp))
        Column(
            modifier = Modifier.weight(2f)
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
        Text(
            text = duration,
            color = MaterialTheme.extendedColours.textSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
