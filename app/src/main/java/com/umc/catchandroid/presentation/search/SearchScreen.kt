package com.umc.catchandroid.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val keyword by viewModel.keyword.collectAsState()
    val results by viewModel.results.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = keyword,
            onValueChange = {
                viewModel.onKeywordChange(it)
                viewModel.search()
            },
            label = { Text("검색어를 입력하세요") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(results) { notice ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = notice.categoryTag)
                        Text(text = notice.title)
                        Text(text = notice.source)
                    }
                }
            }
        }
    }
}
