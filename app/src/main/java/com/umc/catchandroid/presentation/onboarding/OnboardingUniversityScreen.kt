package com.umc.catchandroid.presentation.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.catchandroid.ui.theme.CatchDivider
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle
import androidx.compose.foundation.border
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.HorizontalDivider
import androidx.hilt.navigation.compose.hiltViewModel

private val universities = listOf(
    "동아대학교", "영남대학교", "인제대학교", "경북대학교",
    "부산대학교", "경상대학교", "경희대학교"
)

@Composable
fun OnboardingUniversityScreen(
    onNext: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var selected by remember { mutableStateOf(universities.first()) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(
            text = "대학 선택",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = CatchTextTitle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )

        HorizontalDivider(color = CatchDivider, modifier = Modifier.padding(bottom = 20.dp))

        Text(
            text = "재학 중인 대학을 선택하세요 (이메일 인증 없이 선택)",
            fontSize = 13.sp,
            color = CatchTextCaption,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(universities) { univ ->
                val isSelected = selected == univ
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .selectable(
                            selected = isSelected,
                            onClick = { selected = univ }
                        )
                        .then(
                            Modifier.padding(0.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) CatchPrimary else CatchDivider
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = univ,
                        fontSize = 16.sp,
                        color = CatchTextTitle,
                        modifier = Modifier.weight(1f)
                    )
                    RadioButton(
                        selected = isSelected,
                        onClick = { selected = univ },
                        colors = RadioButtonDefaults.colors(selectedColor = CatchPrimary)
                    )
                }
            }
        }

        Button(
            onClick = {
                viewModel.saveUniversity(selected)
                onNext()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CatchPrimary)
        ) {
            Text("카카오로 시작하기")
        }
    }
}