package com.umc.catchandroid.data.local

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 학교 정보(대학/학과/학년) 변경 이벤트를 전역으로 전파하기 위한 싱글톤.
 * SchoolInfoEditViewModel이 저장 성공 시 notifyChanged()를 호출하면,
 * 이를 구독하는 HomeViewModel 등이 즉시 데이터를 새로고침한다.
 * 네비게이션 백스택 경로(어느 화면에서 진입했는지)와 무관하게 항상 동작한다.
 */
@Singleton
class SchoolInfoChangeNotifier @Inject constructor() {
    private val _changes = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val changes: SharedFlow<Unit> = _changes.asSharedFlow()

    suspend fun notifyChanged() {
        _changes.emit(Unit)
    }
}