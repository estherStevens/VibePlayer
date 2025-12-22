package com.stevens.software.vibeplayer.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.vibeplayer.MediaItemUi
import com.stevens.software.vibeplayer.R
import com.stevens.software.vibeplayer.ui.common.TrackItem
import com.stevens.software.vibeplayer.ui.theme.extendedColours

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    paddingValues: PaddingValues,
    onNavigateBack: () -> Unit,
    onNavigateToPlayer: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                SearchNavigationEvents.NavigateBack -> onNavigateBack()
                is SearchNavigationEvents.NavigateToPlayer -> onNavigateToPlayer(event.id)
            }
        }
    }
    SearchView(
        paddingValues = paddingValues,
        isSearching = uiState.value.isSearching,
        searchedAudio = uiState.value.searchedAudio,
        searchTerm = uiState.value.searchInput,
        onSearchTermUpdated = viewModel::updateSearchTerm,
        onClose = viewModel::onCloseSearchBar,
        onNavigateToPlayer = viewModel::onNavigateToPlayer
    )
}

@Composable
fun SearchView(
    paddingValues: PaddingValues,
    isSearching: Boolean,
    searchedAudio: List<MediaItemUi>,
    searchTerm: String,
    onSearchTermUpdated: (String) -> Unit,
    onClose: () -> Unit,
    onNavigateToPlayer: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColours.bg)
            .padding(paddingValues)
            .padding(top = 12.dp)
    ) {
        SearchBar( //todo maybe move in the lazycolumn
            isSearching = isSearching,
            searchTerm = searchTerm,
            onSearchTermUpdated = onSearchTermUpdated,
            onClose = onClose
        )
        Spacer(Modifier.size(10.dp))
        LazyColumn {
            items(searchedAudio) {
                TrackItem(
                    id = it.id,
                    trackTitle = it.title,
                    artist = it.artist,
                    albumArt = it.albumArt,
                    duration = it.duration,
                    onNavigateToPlayer = onNavigateToPlayer
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.extendedColours.outline,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    isSearching: Boolean,
    searchTerm: String,
    onSearchTermUpdated: (String) -> Unit,
    onClose: () -> Unit
) {
    var searchTerm by remember { mutableStateOf(searchTerm) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchTerm,
            onValueChange = {
                searchTerm = it
                onSearchTermUpdated(it)
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.searchbar_placeholder),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.extendedColours.textSecondary
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            },
            trailingIcon = {
                if (isSearching) {
                    Icon(
                        painter = painterResource(R.drawable.search_close),
                        contentDescription = stringResource(R.string.search_close),
                        tint = Color.Unspecified,
                        modifier = Modifier.clickable {
                            searchTerm = ""
                            onSearchTermUpdated("")
                        }
                    )
                }
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(2f),
            shape = CircleShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.extendedColours.outline,
                unfocusedBorderColor = MaterialTheme.extendedColours.outline,
                focusedContainerColor = MaterialTheme.extendedColours.buttonHover,
                unfocusedContainerColor = MaterialTheme.extendedColours.buttonHover,
                focusedTextColor = MaterialTheme.extendedColours.textPrimary,
            )
        )
        if(isSearching) {
            TextButton(
                onClick = onClose,
            ) {
                Text(
                    text = stringResource(R.string.search_cancel),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.extendedColours.buttonPrimary
                )
            }
        }

    }
}


@Composable
@Preview
fun SearchViewPreview() {
    SearchView(
        paddingValues = PaddingValues(),
        isSearching = false,
        searchTerm = "",
        searchedAudio = emptyList(),
        onSearchTermUpdated = {},
        onClose = {},
        onNavigateToPlayer = {}
    )
}