package com.umc.catchandroid.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.data.local.RecentSearchManager
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val recentSearchManager: RecentSearchManager
) : ViewModel() {

    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> = _keyword.asStateFlow()

    private val _results = MutableStateFlow<List<Notice>>(emptyList())
    val results: StateFlow<List<Notice>> = _results.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val recentSearches: StateFlow<List<String>> = recentSearchManager.recentSearches
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onKeywordChange(newKeyword: String) {
        _keyword.value = newKeyword
    }

    fun search(query: String = _keyword.value) {
        if (query.isBlank()) return
        _keyword.value = query
        viewModelScope.launch {
            _isSearching.value = true
            _errorMessage.value = null
            try {
                recentSearchManager.addSearch(query)
                _results.value = searchRepository.searchNotices(query)
            } catch (e: IOException) {
                _errorMessage.value = "네트워크 연결을 확인해주세요"
            } catch (e: Exception) {
                _errorMessage.value = "검색 중 문제가 발생했어요. 다시 시도해주세요"
            } finally {
                _isSearching.value = false
            }
        }
    }

    fun removeRecentSearch(query: String) {
        viewModelScope.launch {
            recentSearchManager.removeSearch(query)
        }
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            recentSearchManager.clearSearches()
        }
    }
}