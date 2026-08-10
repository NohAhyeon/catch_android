package com.umc.catchandroid.presentation.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.domain.model.Department
import com.umc.catchandroid.ui.theme.CatchInactive
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle

@Composable
fun DepartmentSearchScreen(
    universityId: Long,
    onBack: () -> Unit,
    onDepartmentSelected: (Department) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val departments by viewModel.departments.collectAsState()
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(searchText) {
        if (searchText.isNotEmpty()) {
            viewModel.loadDepartments(universityId, searchText)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 16.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기", tint = CatchTextTitle)
            }
            Text(
                text = "학과 검색",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Box(modifier = Modifier.padding(end = 24.dp))
        }
        HorizontalDivider(color = CatchInactive, thickness = 1.dp)

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("컴퓨터", color = CatchTextCaption) },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "지우기",
                        tint = CatchTextCaption,
                        modifier = Modifier.clickable { searchText = "" }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "검색",
                        tint = CatchPrimary
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatchPrimary,
                unfocusedBorderColor = CatchInactive
            )
        )

        if (searchText.isNotEmpty()) {
            Text(
                text = "검색 결과",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextCaption,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(departments) { dept ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDepartmentSelected(dept) }
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = dept.departmentName,
                        fontSize = 15.sp,
                        color = CatchTextTitle,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = CatchTextCaption
                    )
                }
                HorizontalDivider(color = CatchInactive, thickness = 0.5.dp)
            }
        }
    }
}