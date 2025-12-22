package com.stevens.software.vibeplayer.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.stevens.software.vibeplayer.ui.theme.extendedColours

@Composable
fun SecondaryButton(
    buttonText: Int,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.extendedColours.outline),
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            leadingIcon()
            Text(
                text = stringResource(buttonText),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.extendedColours.textPrimary
            )
        }

    }
}