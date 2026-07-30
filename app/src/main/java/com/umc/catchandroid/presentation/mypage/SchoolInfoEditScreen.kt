package com.umc.catchandroid.presentation.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.R
import com.umc.catchandroid.ui.theme.CatchInactive
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondaryLight
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle

@Composable
fun SchoolInfoEditScreen(
    onBack: () -> Unit,
    viewModel: SchoolInfoEditViewModel = hiltViewModel()
) {
    val universities by viewModel.universities.collectAsState()
    val departments by viewModel.departments.collectAsState()
    val selectedUniversity by viewModel.selectedUniversity.collectAsState()
    val selectedDepartment by viewModel.selectedDepartment.collectAsState()
    val grade by viewModel.grade.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var universitySearch by remember(selectedUniversity) {
        mutableStateOf(selectedUniversity?.universityName ?: "")
    }
    var departmentSearch by remember(selectedDepartment) {
        mutableStateOf(selectedDepartment?.departmentName ?: "")
    }
    var showUniversityList by remember { mutableStateOf(false) }
    var showDepartmentList by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "뒤로", tint = CatchTextTitle)
            }
            Text(
                text = "학교 정보 수정",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("불러오는 중...", color = CatchTextCaption)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                Text(
                    text = "대학",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = CatchTextTitle,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = universitySearch,
                    onValueChange = {
                        universitySearch = it
                        showUniversityList = true
                        viewModel.searchUniversity(it)
                    },
                    placeholder = { Text("대학 검색", color = CatchTextCaption) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CatchPrimary,
                        unfocusedBorderColor = CatchInactive
                    )
                )
                if (showUniversityList && universities.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(top = 4.dp)
                    ) {
                        items(universities) { univ ->
                            Text(
                                text = univ.universityName,
                                fontSize = 14.sp,
                                color = CatchTextTitle,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectUniversity(univ)
                                        universitySearch = univ.universityName
                                        departmentSearch = ""
                                        showUniversityList = false
                                    }
                                    .padding(vertical = 10.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "학과",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = CatchTextTitle,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = departmentSearch,
                    onValueChange = {
                        departmentSearch = it
                        showDepartmentList = true
                        viewModel.searchDepartment(it)
                    },
                    placeholder = { Text("학과 검색", color = CatchTextCaption) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedUniversity != null,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CatchPrimary,
                        unfocusedBorderColor = CatchInactive
                    )
                )
                if (showDepartmentList && departments.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(top = 4.dp)
                    ) {
                        items(departments) { dept ->
                            Text(
                                text = dept.departmentName,
                                fontSize = 14.sp,
                                color = CatchTextTitle,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectDepartment(dept)
                                        departmentSearch = dept.departmentName
                                        showDepartmentList = false
                                    }
                                    .padding(vertical = 10.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "학년",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = CatchTextTitle,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = grade,
                    onValueChange = { viewModel.updateGrade(it) },
                    placeholder = { Text("3학년", color = CatchTextCaption) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CatchPrimary,
                        unfocusedBorderColor = CatchInactive
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .background(color = CatchSecondaryLight, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.character2),
                            contentDescription = "공지캐치 캐릭터",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.height(56.dp)
                        )
                        Text(
                            text = "학교 정보를 수정하면 더 정확한 맞춤 공지를 받을 수 있어요!",
                            fontSize = 13.sp,
                            color = CatchTextTitle,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {}

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp, top = 16.dp)
                        .height(56.dp)
                        .background(color = CatchPrimary, shape = RoundedCornerShape(12.dp))
                        .clickable {
                            if (selectedUniversity != null && selectedDepartment != null) {
                                viewModel.save(onDone = onBack)
                            }
                        }
                ) {
                    Text(
                        text = "저장하기",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}