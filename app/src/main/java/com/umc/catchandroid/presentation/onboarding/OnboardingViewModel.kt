package com.umc.catchandroid.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.model.Department
import com.umc.catchandroid.domain.model.University
import com.umc.catchandroid.domain.repository.UniversityRepository
import com.umc.catchandroid.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val universityRepository: UniversityRepository
) : ViewModel() {

    private val _universities = MutableStateFlow<List<University>>(emptyList())
    val universities: StateFlow<List<University>> = _universities.asStateFlow()

    private val _departments = MutableStateFlow<List<Department>>(emptyList())
    val departments: StateFlow<List<Department>> = _departments.asStateFlow()

    private val _selectedUniversityId = MutableStateFlow<Long?>(null)
    val selectedUniversityId: StateFlow<Long?> = _selectedUniversityId.asStateFlow()

    fun loadUniversities() {
        viewModelScope.launch {
            _universities.value = universityRepository.getUniversities()
        }
    }

    fun loadDepartments(universityId: Long, keyword: String? = null) {
        viewModelScope.launch {
            _departments.value = universityRepository.getDepartments(universityId, keyword)
        }
    }

    fun saveUniversity(universityName: String) {
        val university = _universities.value.find { it.universityName == universityName }
        _selectedUniversityId.value = university?.universityId
        viewModelScope.launch {
            university?.let {
                universityRepository.selectUniversity(it.universityId)
            }
        }
    }

    fun saveProfile(departmentId: Long, grade: Int) {
        viewModelScope.launch {
            userRepository.updateOnboardingInfo(departmentId = departmentId, grade = grade)
        }
    }

    fun saveKeywords(
        recommended: List<String>,
        custom: List<String>,
        onDone: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val keywords = recommended.map { it to "RECOMMEND" } + custom.map { it to "CUSTOM" }
            userRepository.updateKeywords(keywords)
            onDone()
        }
    }
}