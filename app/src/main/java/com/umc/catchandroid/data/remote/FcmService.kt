package com.umc.catchandroid.data.remote

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.umc.catchandroid.MainActivity
import com.umc.catchandroid.R
import com.umc.catchandroid.domain.repository.NotificationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val CHANNEL_ID = "catch_default_channel"

@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepository: NotificationRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    // 앱이 새로 설치되거나, 토큰이 갱신될 때 호출됨 -> 서버에 최신 토큰 저장
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        serviceScope.launch {
            try {
                notificationRepository.registerDeviceToken(token)
            } catch (e: Exception) {
                // 로그인 전이거나 네트워크 문제로 실패할 수 있음 - 조용히 무시
                // (로그인 성공 시점에 NotificationBadgeViewModel에서 한 번 더 등록 시도함)
            }
        }
    }

    // 앱이 포그라운드에 있을 때 푸시가 도착하면 호출됨
    // (백그라운드/종료 상태에서는 시스템이 알아서 알림 트레이에 띄워줌)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: message.data["title"] ?: "공지캐치"
        val body = message.notification?.body ?: message.data["message"] ?: ""
        val noticeId = message.data["noticeId"]?.toLongOrNull()

        showNotification(title, body, noticeId)
    }

    private fun showNotification(title: String, body: String, noticeId: Long?) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "공지캐치 알림",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            noticeId?.let { putExtra("noticeId", it) }
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            noticeId?.toInt() ?: 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notice2)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationId = noticeId?.toInt() ?: System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)
    }
}