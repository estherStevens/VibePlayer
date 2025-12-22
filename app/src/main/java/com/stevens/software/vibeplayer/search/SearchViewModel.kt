package com.stevens.software.vibeplayer.search

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.vibeplayer.MediaItemUi
import com.stevens.software.vibeplayer.core.AudioFile
import com.stevens.software.vibeplayer.core.AudioFileRepository
import com.stevens.software.vibeplayer.utils.toMinutesSeconds
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.map
import kotlin.time.Duration.Companion.milliseconds

class SearchViewModel(
    private val audioFileRepository: AudioFileRepository
): ViewModel() {

    private val _searchTerm: MutableStateFlow<String> = MutableStateFlow("")
    private val _isSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _audioFiles: StateFlow<List<MediaItemUi>> = combine(_isSearching, _searchTerm, audioFileRepository.getAllAudioFile())
    { isSearching, searchTerm, audioFiles ->
        if(isSearching) {
            audioFiles.filter { it.title.lowercase().contains(searchTerm.lowercase()) || it.artist.lowercase().contains(searchTerm.lowercase()) }.map { track -> track.toUi() }
        } else {
            emptyList()
        }

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private val _navigationEvents: MutableSharedFlow<SearchNavigationEvents> = MutableSharedFlow()
    val navigationEvents = _navigationEvents.asSharedFlow()

    val uiState: StateFlow<SearchUiState> = combine(_isSearching, _searchTerm, _audioFiles)
    { isSearching, searchTerm, searchedAudioFiles ->
        SearchUiState(
            isSearching = isSearching,
            searchInput = searchTerm,
            searchedAudio = searchedAudioFiles
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        SearchUiState(
            isSearching = false,
            searchInput = "",
            searchedAudio = emptyList()
        )
    )

    fun AudioFile.toUi() = MediaItemUi(
        id = this.id,
        title = this.title,
        albumArt = this.artworkUri.toUri(),
        artist = this.artist,
        duration = this.duration.milliseconds.toMinutesSeconds()
    ) //todo move to repo


    fun updateSearchTerm(searchTerm: String){
        _isSearching.update { true }
        _searchTerm.update { searchTerm }
    }

    fun onCloseSearchBar(){
        viewModelScope.launch {
            _navigationEvents.emit(SearchNavigationEvents.NavigateBack)
        }
    }

    fun onNavigateToPlayer(id: String){
        viewModelScope.launch {
            _navigationEvents.emit(SearchNavigationEvents.NavigateToPlayer(id))
        }
    }
}

data class SearchUiState(
    val isSearching: Boolean,
    val searchInput: String,
    val searchedAudio: List<MediaItemUi>
)

sealed interface SearchNavigationEvents{
    object NavigateBack: SearchNavigationEvents
    data class NavigateToPlayer(val id: String): SearchNavigationEvents
}