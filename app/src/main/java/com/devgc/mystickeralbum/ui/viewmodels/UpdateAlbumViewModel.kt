package com.devgc.mystickeralbum.ui.viewmodels

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devgc.mystickeralbum.AlbumsRepository
import com.devgc.mystickeralbum.MyStickerAlbumApplication
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.StickersList
import com.devgc.mystickeralbum.model.TextFieldValues
import com.devgc.mystickeralbum.navigation.MainNavComponent.Companion.albumNameArgument
import com.devgc.mystickeralbum.navigation.MainNavComponent.Companion.navController
import com.devgc.mystickeralbum.navigation.NavigationParameters
import com.devgc.mystickeralbum.navigation.screens.EditAlbumScreen
import com.devgc.mystickeralbum.ui.stateholders.StickerFilter
import com.devgc.mystickeralbum.ui.stateholders.UpdateAlbumUIState
import com.devgc.mystickeralbum.ui.stateholders.WorldCupQuickFilterOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UpdateAlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val topUIItems = 4
        private const val portraitColumnsGrid = 5
        private const val landscapeColumnsGrid = 10
    }

    private val _uiState: MutableStateFlow<UpdateAlbumUIState> =
        MutableStateFlow(UpdateAlbumUIState())
    val uiState get() = _uiState.asStateFlow()

    private var currentFilter: StickerFilter = StickerFilter.All


    init {
        _uiState.update {
            it.copy(
                onStickerClick = ::onStickerClick,
                onRemoveSticker = ::onRemoveSticker,
                onToggleStickerLineBreak = ::onToggleStickerLineBreak,
                onToggleStickerExtraLine = ::onToggleStickerExtraLine,
                onCloseDeleteAlbumDialog = ::onCloseDeleteAlbumDialog,
                onConfirmDeleteAlbumDialog = ::onConfirmDeleteAlbumDialog,
                changeIconsLegendDialogState = ::changeIconsLegendDialogState,
                searchStickerTextField = TextFieldValues(onTextChange = ::onSearchStickerChange),
                onSearchStickerClick = ::onSearchStickerClick,
                onClearTextField = ::onClearTextField,
                onScroll = ::onScroll,
                onReturnToTopButtonClick = ::onReturnToTopButtonClick,
                onColumnsChanged = ::onColumnsChanged,
                onToggleHeader = ::onToggleHeader,
                onWorldCupQuickFilterOrderSelected = ::onWorldCupQuickFilterOrderSelected,
                onWorldCupTeamSelected = ::onWorldCupTeamSelected
            )
        }

        CoroutineScope(IO).launch {
            savedStateHandle
                .getStateFlow<String?>(albumNameArgument, null)
                .filterNotNull()
                .collect { albumName ->
                    onReceiveAlbumName(albumName)
                }
        }
    }

    private fun onReceiveAlbumName(albumName: String) {
        viewModelScope.launch {
            val album = withContext(IO) {
                return@withContext AlbumsRepository.getAlbumByName(albumName)
            } ?: return@launch

            _uiState.update {
                it.copy(
                    album = album,
                    stickers = getFilteredStickers(
                        stickers = album.stickersList.stickers,
                        filterType = currentFilter,
                        text = it.searchStickerTextField.text
                    ),
                    columns = album.selectedColumns,
                    selectedFilter = currentFilter
                )
            }
        }
    }

    private fun onStickerClick(sticker: Sticker) {
        if (!sticker.found) {
            onFoundNotFoundClick(true, sticker)
        } else {
            onChangeRepeatedStickerClick(1, sticker)
        }
    }

    private fun onRemoveSticker(sticker: Sticker) {
        if (sticker.repeated > 0) {
            onChangeRepeatedStickerClick(-1, sticker)
        } else {
            onFoundNotFoundClick(false, sticker)
        }
    }

    private fun onToggleStickerLineBreak(sticker: Sticker) {
        updateSticker(
            sticker.copy(
                lineBreakAfter = !sticker.lineBreakAfter,
                extraLineAfter = if (sticker.lineBreakAfter) false else sticker.extraLineAfter
            )
        )
    }

    private fun onToggleStickerExtraLine(sticker: Sticker) {
        updateSticker(
            sticker.copy(
                lineBreakAfter = true,
                extraLineAfter = !sticker.extraLineAfter
            )
        )
    }

    private fun onFoundNotFoundClick(found: Boolean, sticker: Sticker) {
        val newSticker = sticker.copy(found = found, repeated = 0)
        updateSticker(newSticker)
    }

    private fun onChangeRepeatedStickerClick(value: Int, sticker: Sticker) {
        val newRepeated = sticker.repeated + value

        if (newRepeated >= 0) {
            val newSticker = sticker.copy(repeated = newRepeated)
            updateSticker(newSticker)
        }
    }

    private fun updateSticker(newSticker: Sticker) {
        val currentState = _uiState.value
        val newStickers = ArrayList(currentState.album.stickersList.stickers)

        newStickers.replaceAll {
            if (it.identifier == newSticker.identifier) {
                newSticker
            } else {
                it
            }
        }

        val newAlbum = currentState.album.copy(
            stickersList = StickersList(newStickers)
        )
        val filteredStickers = getFilteredStickers(
            stickers = newStickers,
            filterType = currentFilter,
            text = currentState.searchStickerTextField.text
        )

        _uiState.update {
            it.copy(
                album = newAlbum,
                stickers = filteredStickers,
                selectedFilter = currentFilter
            )
        }

        viewModelScope.launch {
            withContext(IO) {
                AlbumsRepository.updateStickers(newAlbum)
            }
        }
    }

    fun onDeleteAlbumClick() {
        _uiState.update {
            it.copy(
                showDeleteAlbumDialog = true
            )
        }
    }

    private fun onCloseDeleteAlbumDialog() {
        _uiState.update {
            it.copy(
                showDeleteAlbumDialog = false
            )
        }
    }

    private fun onConfirmDeleteAlbumDialog() {
        viewModelScope.launch {
            withContext(IO) {
                AlbumsRepository.removeAlbum(_uiState.value.album)
            }

            navController.popBackStack()
        }
    }

    fun onEditAlbumClick() {
        navController.apply {
            EditAlbumScreen.apply {
                navigateToItself(
                    parameters = NavigationParameters(
                        albumName = _uiState.value.album.name
                    )
                )
            }
        }
    }

    fun changeIconsLegendDialogState() {
        _uiState.update {
            it.copy(
                showIconsLegendDialog = !_uiState.value.showIconsLegendDialog
            )
        }
    }

    private fun onSearchStickerChange(text: String) {
        _uiState.update {
            it.copy(
                searchStickerTextField = it.searchStickerTextField.copy(
                    text = text
                ),
                stickers = getFilteredStickers(
                    stickers = it.album.stickersList.stickers,
                    filterType = currentFilter,
                    text = text
                ),
                selectedFilter = currentFilter
            )
        }
    }

    private fun onSearchStickerClick(lazyListState: LazyListState, scope: CoroutineScope) {
        val stickerText = _uiState.value.searchStickerTextField.text.uppercase()
        val stickerIndex =
            _uiState.value.album.stickersList.stickers.indexOfFirst { it.identifier == stickerText }
        val columns = (_uiState.value.columns ?: getDefaultColumnsGrid()).coerceAtLeast(1)
        val stickerRowIndex = if (stickerIndex == -1) -1 else stickerIndex / columns
        val index = if (stickerRowIndex == -1) 0 else stickerRowIndex + topUIItems

        scope.launch {
            lazyListState.animateScrollToItem(index)
            onScroll(index)
        }

        onSearchStickerChange("")

        if (stickerIndex == -1) {
            val context = MyStickerAlbumApplication.getInstance()
            Toast.makeText(
                context,
                context.resources.getString(R.string.search_sticker_not_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onScroll(index: Int) {
        val showReturnToTopButton = index >= topUIItems
        if (_uiState.value.showReturnToTopButton == showReturnToTopButton) {
            return
        }

        _uiState.update {
            it.copy(
                showReturnToTopButton = showReturnToTopButton
            )
        }
    }

    private fun onClearTextField() {
        onSearchStickerChange("")
    }

    private fun onWorldCupTeamSelected(code: String) {
        onSearchStickerChange(code)
    }

    fun onStickerFilterSelected(filter: StickerFilter) {
        filterStickers(filter)
    }

    fun isStickerFilterSelected(filter: StickerFilter): Boolean {
        return _uiState.value.selectedFilter == filter
    }

    private fun onWorldCupQuickFilterOrderSelected(order: WorldCupQuickFilterOrder) {
        if (_uiState.value.worldCupQuickFilterOrder == order) {
            return
        }

        _uiState.update {
            it.copy(worldCupQuickFilterOrder = order)
        }
    }

    private fun onReturnToTopButtonClick(lazyListState: LazyListState, scope: CoroutineScope) {
        scope.launch {
            lazyListState.animateScrollToItem(0)
        }

        _uiState.update {
            it.copy(
                showReturnToTopButton = false
            )
        }
    }

    private fun onColumnsChanged(columns: Int?) {
        val currentState = _uiState.value
        val normalizedColumns = columns?.coerceAtLeast(1)
        if (currentState.columns == normalizedColumns) {
            return
        }

        val newAlbum = currentState.album.copy(selectedColumns = normalizedColumns)
        _uiState.update {
            it.copy(
                album = newAlbum,
                columns = normalizedColumns
            )
        }

        viewModelScope.launch {
            withContext(IO) {
                AlbumsRepository.updateAlbum(newAlbum)
            }
        }
    }

    private fun getDefaultColumnsGrid(): Int {
        val orientation = MyStickerAlbumApplication.getInstance().resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            landscapeColumnsGrid
        } else {
            portraitColumnsGrid
        }
    }

    private fun filterStickers(filterType: StickerFilter, text: String = _uiState.value.searchStickerTextField.text) {
        currentFilter = filterType

        _uiState.update {
            it.copy(
                stickers = getFilteredStickers(
                    stickers = it.album.stickersList.stickers,
                    filterType = filterType,
                    text = text
                ),
                selectedFilter = filterType
            )
        }

    }

    private fun getFilteredStickers(
        stickers: List<Sticker>,
        filterType: StickerFilter,
        text: String
    ): List<Sticker> {
        val query = text.trim()
        val hasQuery = query.isNotEmpty()

        if (!hasQuery && filterType == StickerFilter.All) {
            return stickers
        }

        return stickers.filter { sticker ->
            val matchesFilter = when (filterType) {
                StickerFilter.All -> true
                StickerFilter.Missing -> !sticker.found
                StickerFilter.Repeated -> sticker.found && sticker.repeated > 0
            }
            val matchesText = !hasQuery || sticker.identifier.contains(query, ignoreCase = true)

            matchesFilter && matchesText
        }
    }

    private fun onToggleHeader() {
        _uiState.update {
            it.copy(isHeaderVisible = !it.isHeaderVisible)
        }
    }
}
