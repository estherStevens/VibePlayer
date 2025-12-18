package com.stevens.software.vibeplayer.ui.theme

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
@Composable
fun PrimaryButton(
    buttonText: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.extendedColours.buttonPrimary
        ),
        modifier = modifier
    ) {
        Text(
            text = stringResource(buttonText),
            color = MaterialTheme.extendedColours.textPrimary,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}