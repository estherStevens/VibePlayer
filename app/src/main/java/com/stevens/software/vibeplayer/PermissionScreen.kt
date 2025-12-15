package com.stevens.software.vibeplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.stevens.software.vibeplayer.ui.theme.PrimaryButton
import com.stevens.software.vibeplayer.ui.theme.extendedColours

@Composable
fun PermissionScreen(
    onNavigateToVibePlayer: () -> Unit
) {
    val context = LocalContext.current
    val permission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(isGranted) {
            onNavigateToVibePlayer()
        }
    }

    val hasReadMediaPermission =
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    if (hasReadMediaPermission) {
        onNavigateToVibePlayer()
    }

    PermissionView(
        onLaunchPermissionDialog = {
            launcher.launch(permission)
        }
    )
}

@Composable
internal fun PermissionView(
    onLaunchPermissionDialog: () -> Unit,
){
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
                buttonText = R.string.permission_allow_access,
                onClick = onLaunchPermissionDialog
            )
        }
    }
}


@Composable
@Preview(showSystemUi = true)
fun PermissionViewPreview(){
    PermissionView(
        onLaunchPermissionDialog = {}
    )
}