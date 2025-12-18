package com.stevens.software.vibeplayer.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ScanViewModel : ViewModel() {

    private val _selectedFileDurationOption: MutableStateFlow<FileDurationOption> = MutableStateFlow(FileDurationOption.THIRTY_SECONDS)
    private val _selectedFileSizeOption: MutableStateFlow<FileSizeOption> = MutableStateFlow(FileSizeOption.ONE_HUNDRED_KBS)

    val uiState: StateFlow<ScanUiState> = combine(
        _selectedFileDurationOption, _selectedFileSizeOption
    ){ selectedFileDurationOption, selectedFileSizeOption ->
        ScanUiState(
            fileDurationOptions = fileDurationOptions(),
            fileSizeOptions = fileSizeOptions(),
            selectedFileDurationOption = selectedFileDurationOption,
            selectedFileSizeOption = selectedFileSizeOption
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        ScanUiState(
            fileDurationOptions = emptyList(),
            fileSizeOptions = emptyList(),
            selectedFileDurationOption = FileDurationOption.THIRTY_SECONDS,
            selectedFileSizeOption = FileSizeOption.ONE_HUNDRED_KBS
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
}

data class ScanUiState(
    val fileDurationOptions: List<FileDurationOption>,
    val fileSizeOptions: List<FileSizeOption>,
    val selectedFileDurationOption: FileDurationOption,
    val selectedFileSizeOption: FileSizeOption
)

enum class FileDurationOption {
    THIRTY_SECONDS,
    SIXTY_SECONDS
}

enum class FileSizeOption {
    ONE_HUNDRED_KBS,
    FIVE_HUNDRED_KBS
}