package com.stevens.software.vibeplayer.scan

import android.widget.RadioGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.vibeplayer.R
import com.stevens.software.vibeplayer.ui.common.Scanner
import com.stevens.software.vibeplayer.ui.theme.PrimaryButton
import com.stevens.software.vibeplayer.ui.theme.extendedColours

@Composable
fun ScanScreen(
    viewModel: ScanViewModel,
    onBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    ScanView(
        fileDurationOptions = uiState.value.fileDurationOptions,
        fileSizeOptions = uiState.value.fileSizeOptions,
        selectedFileDurationOption = uiState.value.selectedFileDurationOption,
        selectedFileSizeOption = uiState.value.selectedFileSizeOption,
        onBack = onBack,
        onSelectedFileSize = viewModel::updateSelectedFileSizeOption,
        onSelectedFileDuration = viewModel::updateSelectedFileDurationOption
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanView(
    fileDurationOptions: List<FileDurationOption>,
    fileSizeOptions: List<FileSizeOption>,
    selectedFileDurationOption: FileDurationOption,
    selectedFileSizeOption: FileSizeOption,
    onBack: () -> Unit,
    onSelectedFileSize: (FileSizeOption) -> Unit,
    onSelectedFileDuration: (FileDurationOption) -> Unit
) {
    var selectedFileDuration by remember { mutableStateOf(selectedFileDurationOption) }
    var selectedFileSize by remember { mutableStateOf(selectedFileSizeOption) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColours.bg)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.scan_music),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.extendedColours.textPrimary
                )
            },
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = MaterialTheme.extendedColours.bg
            ),
            navigationIcon = {
                Icon(
                    painter = painterResource(R.drawable.back),
                    contentDescription = stringResource(R.string.back),
                    tint = Color.Unspecified,
                    modifier = Modifier.clickable {
                        onBack()
                    }
                )
            }
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Scanner(startAnimation = false)
            Spacer(Modifier.size(24.dp))
            Text(
                text = stringResource(R.string.scan_music_duration),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.extendedColours.textSecondary,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.size(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                fileDurationOptions.forEach { option ->
                    RadioButtonItem(
                        radioButtonText = when(option){
                            FileDurationOption.THIRTY_SECONDS -> R.string.scan_music_duration_30s
                            FileDurationOption.SIXTY_SECONDS -> R.string.scan_music_duration_60s
                        },
                        selected = option == selectedFileDuration,
                        onClick = {
                            selectedFileDuration = option
                            onSelectedFileDuration(option)
                        }
                    )

                }
            }
            Spacer(Modifier.size(10.dp))
            Text(
                text = stringResource(R.string.scan_music_size),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.extendedColours.textSecondary,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.size(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                fileSizeOptions.forEach { option ->
                    RadioButtonItem(
                        radioButtonText = when(option){
                            FileSizeOption.ONE_HUNDRED_KBS -> R.string.scan_music_size_100kb
                            FileSizeOption.FIVE_HUNDRED_KBS -> R.string.scan_music_size_500kb
                        },
                        selected = option == selectedFileSize,
                        onClick = {
                            selectedFileSize = option
                            onSelectedFileSize(option)
                        }
                    )
                }
            }
            Spacer(Modifier.size(24.dp))
            PrimaryButton(
                buttonText = R.string.scan,
                modifier = Modifier.fillMaxWidth(),
                onClick = {}
            )


        }

    }
}

@Composable
private fun RowScope.RadioButtonItem(
    radioButtonText: Int,
    selected: Boolean,
    onClick: () -> Unit
){
    val boxBorderColour = when(selected){
        true -> MaterialTheme.extendedColours.buttonPrimary.copy(alpha = 0.3f)
        false -> MaterialTheme.extendedColours.outline
    }
    Box(
        modifier = Modifier
            .weight(1f)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = boxBorderColour
                ),
                shape = CircleShape
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors().copy(
                    selectedColor = MaterialTheme.extendedColours.buttonPrimary,
                    unselectedColor = MaterialTheme.extendedColours.textSecondary
                )
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text = stringResource(radioButtonText),
                color = MaterialTheme.extendedColours.textPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun ScanViewPreview() {
    ScanView(
        fileDurationOptions = listOf(
            FileDurationOption.THIRTY_SECONDS,
            FileDurationOption.SIXTY_SECONDS
        ),
        fileSizeOptions = listOf(
            FileSizeOption.ONE_HUNDRED_KBS,
            FileSizeOption.FIVE_HUNDRED_KBS
        ),
        selectedFileDurationOption = FileDurationOption.THIRTY_SECONDS,
        selectedFileSizeOption = FileSizeOption.ONE_HUNDRED_KBS,
        onSelectedFileDuration = {},
        onSelectedFileSize = {},
        onBack = {}
    )
}