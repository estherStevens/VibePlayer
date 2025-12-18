package com.stevens.software.vibeplayer.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stevens.software.vibeplayer.media.MediaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanViewModel(
    private val mediaRepository: MediaRepository,
) : ViewModel() {

    private val _selectedFileDurationOption: MutableStateFlow<FileDurationOption> = MutableStateFlow(FileDurationOption.THIRTY_SECONDS)
    private val _selectedFileSizeOption: MutableStateFlow<FileSizeOption> = MutableStateFlow(FileSizeOption.ONE_HUNDRED_KBS)
    private val _isScanning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _navigationEvents: MutableSharedFlow<ScanNavigationEvents> = MutableSharedFlow()
    val navigationEvents = _navigationEvents.asSharedFlow()

    val uiState: StateFlow<ScanUiState> = combine(
        _selectedFileDurationOption, _selectedFileSizeOption, _isScanning, mediaRepository.mediaItems
    ){ selectedFileDurationOption, selectedFileSizeOption, isScanning, mediaItems ->
        ScanUiState(
            fileDurationOptions = fileDurationOptions(),
            fileSizeOptions = fileSizeOptions(),
            selectedFileDurationOption = selectedFileDurationOption,
            selectedFileSizeOption = selectedFileSizeOption,
            isScanning = isScanning
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        ScanUiState(
            fileDurationOptions = emptyList(),
            fileSizeOptions = emptyList(),
            selectedFileDurationOption = FileDurationOption.THIRTY_SECONDS,
            selectedFileSizeOption = FileSizeOption.ONE_HUNDRED_KBS,
            isScanning = false
        )
    )

    fun updateSelectedFileDurationOption(selectedOption: FileDurationOption){
        _selectedFileDurationOption.update { selectedOption }
    }

    fun updateSelectedFileSizeOption(selectedOption: FileSizeOption){
        _selectedFileSizeOption.update { selectedOption }
    }

    fun fileDurationOptions() = listOf(
        FileDurationOption.THIRTY_SECONDS,
        FileDurationOption.SIXTY_SECONDS
    )

    fun fileSizeOptions() = listOf(
        FileSizeOption.ONE_HUNDRED_KBS,
        FileSizeOption.FIVE_HUNDRED_KBS
    )

    fun startScan(){
        viewModelScope.launch {
            _isScanning.update { true }
            delay(1000)
            val result = mediaRepository.fetchFilteredMedia(
                minFileSizeInMs = _selectedFileSizeOption.value.sizeInBytes,
                minFileDurationInMs = _selectedFileDurationOption.value.sizeInMs
            )
            if(result) {
                _navigationEvents.emit(ScanNavigationEvents.NavigateToTrackListing)
            }
        }

    }
}

data class ScanUiState(
    val fileDurationOptions: List<FileDurationOption>,
    val fileSizeOptions: List<FileSizeOption>,
    val selectedFileDurationOption: FileDurationOption,
    val selectedFileSizeOption: FileSizeOption,
    val isScanning: Boolean
)

enum class FileDurationOption(val sizeInMs: Int) {
    THIRTY_SECONDS(30_000),
    SIXTY_SECONDS(60_000)
}

enum class FileSizeOption(val sizeInBytes: Int) {
    ONE_HUNDRED_KBS(102_400),
    FIVE_HUNDRED_KBS(512_000)
}

sealed interface ScanNavigationEvents {
    object NavigateToTrackListing: ScanNavigationEvents
}