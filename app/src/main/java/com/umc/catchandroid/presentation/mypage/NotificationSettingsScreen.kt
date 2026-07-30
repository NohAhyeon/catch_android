package com.umc.catchandroid.presentation.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.domain.model.AlarmSettings
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle

@Composable
fun NotificationSettingsScreen(
    onBack: () -> Unit,
    viewModel: NotificationSettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "뒤로", tint = CatchTextTitle)
            }
            Text(
                text = "알림 설정",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (isLoading || settings == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("불러오는 중...", color = CatchTextCaption)
            }
        } else {
            val current = settings!!
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                AlarmToggleRow(
                    label = "전체 알림 (마스터)",
                    checked = current.isAll,
                    enabled = true,
                    onCheckedChange = { viewModel.toggleAll(it) }
                )
                AlarmToggleRow(
                    label = "마감임박 알림",
                    checked = current.isClosing,
                    enabled = current.isAll,
                    onCheckedChange = { viewModel.toggleClosing(it) }
                )
                AlarmToggleRow(
                    label = "키워드 알림",
                    checked = current.isKeyword,
                    enabled = current.isAll,
                    onCheckedChange = { viewModel.toggleKeyword(it) }
                )
                AlarmToggleRow(
                    label = "장학 알림",
                    checked = current.scholarship,
                    enabled = current.isAll,
                    onCheckedChange = { viewModel.toggleScholarship(it) }
                )
                AlarmToggleRow(
                    label = "비교과 알림",
                    checked = current.extracurricular,
                    enabled = current.isAll,
                    onCheckedChange = { viewModel.toggleExtracurricular(it) }
                )
                AlarmToggleRow(
                    label = "학사 알림",
                    checked = current.academic,
                    enabled = current.isAll,
                    onCheckedChange = { viewModel.toggleAcademic(it) }
                )
                AlarmToggleRow(
                    label = "취업 알림",
                    checked = current.employment,
                    enabled = current.isAll,
                    onCheckedChange = { viewModel.toggleEmployment(it) }
                )
            }
        }
    }
}

@Composable
private fun AlarmToggleRow(
    label: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = if (enabled) CatchTextTitle else CatchTextCaption,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(checkedTrackColor = CatchPrimary)
        )
    }
}