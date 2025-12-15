package com.stevens.software.vibeplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stevens.software.vibeplayer.ui.theme.extendedColours

@Composable
fun PermissionScreen() {
    PermissionView()
}

@Composable
internal fun PermissionView(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.extendedColours.bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.extendedColours.textPrimary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text = stringResource(R.string.permission_reason),
                color = MaterialTheme.extendedColours.textPrimary,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.size(20.dp))
            PrimaryButton(
                onClick = {}
            )
        }
    }
}

@Composable
private fun PrimaryButton(
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.extendedColours.buttonPrimary
        )
    ) {
        Text(
            text = stringResource(R.string.permission_allow_access),
            color = MaterialTheme.extendedColours.textPrimary,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun PermissionViewPreview(){
    PermissionView()
}