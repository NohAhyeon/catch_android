package com.umc.catchandroid.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val deadlineDates by viewModel.deadlineDates.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val noticesForDate by viewModel.noticesForDate.collectAsState()

    // 2026년 7월 기준 (1일이 수요일, 31일까지)
    val daysInMonth = 31
    val startOffset = 3 // 수요일 시작이라고 가정 (0=일요일)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "2026년 7월",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(startOffset) {
                Box(modifier = Modifier.aspectRatio(1f))
            }
            items(daysInMonth) { index ->
                val day = index + 1
                val dateStr = "2026-07-%02d".format(day)
                val hasDeadline = deadlineDates.contains(dateStr)
                val isSelected = selectedDate == dateStr

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(2.dp)
                        .clickable { viewModel.onDateSelected(dateStr) }
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else androidx.compose.ui.graphics.Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = day.toString(),
                            textAlign = TextAlign.Center,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurface
                        )
                        if (hasDeadline) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .aspectRatio(1f)
                                    .background(
                                        color = MaterialTheme.colorScheme.error,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "마감 공지",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        LazyColumn {
            items(noticesForDate) { notice ->
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