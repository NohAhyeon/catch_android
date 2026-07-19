package com.umc.catchandroid.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> = _keyword.asStateFlow()

    private val _results = MutableStateFlow<List<Notice>>(emptyList())
    val results: StateFlow<List<Notice>> = _results.asStateFlow()

    private val _recentSearches = MutableStateFlow(listOf("국가장학금", "교환학생", "근로장학"))
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    fun onKeywordChange(newKeyword: String) {
        _keyword.value = newKeyword
    }

    fun search(query: String = _keyword.value) {
        if (query.isBlank()) return
        _keyword.value = query
        if (!_recentSearches.value.contains(query)) {
            _recentSearches.value = listOf(query) + _recentSearches.value
        }
        viewModelScope.launch {
            _results.value = searchRepository.searchNotices(query)
        }
    }

    fun removeRecentSearch(query: String) {
        _recentSearches.value = _recentSearches.value - query
    }

    fun clearRecentSearches() {
        _recentSearches.value = emptyList()
    }
}