package com.umc.catchandroid.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.searchDataStore by preferencesDataStore(name = "search_prefs")

@Singleton
class RecentSearchManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val RECENT_SEARCHES = stringPreferencesKey("recent_searches")
    private val DELIMITER = "||"
    private val MAX_COUNT = 10

    val recentSearches: Flow<List<String>> = context.searchDataStore.data.map { prefs ->
        prefs[RECENT_SEARCHES]
            ?.split(DELIMITER)
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }

    suspend fun addSearch(query: String) {
        context.searchDataStore.edit { prefs ->
            val current = prefs[RECENT_SEARCHES]
                ?.split(DELIMITER)
                ?.filter { it.isNotBlank() }
                ?: emptyList()
            // 중복이면 기존 걸 지우고 맨 앞으로, 최대 MAX_COUNT개까지만 유지
            val updated = (listOf(query) + current.filterNot { it == query }).take(MAX_COUNT)
            prefs[RECENT_SEARCHES] = updated.joinToString(DELIMITER)
        }
    }

    suspend fun removeSearch(query: String) {
        context.searchDataStore.edit { prefs ->
            val current = prefs[RECENT_SEARCHES]
                ?.split(DELIMITER)
                ?.filter { it.isNotBlank() }
                ?: emptyList()
            prefs[RECENT_SEARCHES] = (current - query).joinToString(DELIMITER)
        }
    }

    suspend fun clearSearches() {
        context.searchDataStore.edit { prefs ->
            prefs.remove(RECENT_SEARCHES)
        }
    }
}