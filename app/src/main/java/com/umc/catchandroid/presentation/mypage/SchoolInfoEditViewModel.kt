package com.umc.catchandroid.presentation.mypage

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
class SchoolInfoEditViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val universityRepository: UniversityRepository
) : ViewModel() {

    private var allUniversities: List<University> = emptyList()

    private val _universities = MutableStateFlow<List<University>>(emptyList())
    val universities: StateFlow<List<University>> = _universities.asStateFlow()

    private val _departments = MutableStateFlow<List<Department>>(emptyList())
    val departments: StateFlow<List<Department>> = _departments.asStateFlow()

    private val _selectedUniversity = MutableStateFlow<University?>(null)
    val selectedUniversity: StateFlow<University?> = _selectedUniversity.asStateFlow()

    private val _selectedDepartment = MutableStateFlow<Department?>(null)
    val selectedDepartment: StateFlow<Department?> = _selectedDepartment.asStateFlow()

    private val _grade = MutableStateFlow("")
    val grade: StateFlow<String> = _grade.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadCurrentProfile()
    }

    private fun loadCurrentProfile() {
        viewModelScope.launch {
            val profile = userRepository.getUserProfile()
            _grade.value = profile.grade.toString()

            allUniversities = universityRepository.getUniversities()
            _universities.value = allUniversities

            val currentUniv = allUniversities.find { it.universityName == profile.universityName }
            _selectedUniversity.value = currentUniv

            if (currentUniv != null) {
                val depts = universityRepository.getDepartments(currentUniv.universityId)
                _departments.value = depts
                _selectedDepartment.value = depts.find { it.departmentName == profile.departmentName }
            }

            _isLoading.value = false
        }
    }

    fun searchUniversity(keyword: String) {
        _universities.value = if (keyword.isBlank()) {
            allUniversities
        } else {
            allUniversities.filter { it.universityName.contains(keyword) }
        }
    }

    fun selectUniversity(university: University) {
        _selectedUniversity.value = university
        _selectedDepartment.value = null
        _departments.value = emptyList()
        viewModelScope.launch {
            _departments.value = universityRepository.getDepartments(university.universityId)
        }
    }

    fun searchDepartment(keyword: String) {
        val universityId = _selectedUniversity.value?.universityId ?: return
        viewModelScope.launch {
            _departments.value = universityRepository.getDepartments(universityId, keyword)
        }
    }

    fun selectDepartment(department: Department) {
        _selectedDepartment.value = department
    }

    fun updateGrade(value: String) {
        _grade.value = value
    }

    fun save(onDone: () -> Unit) {
        val universityId = _selectedUniversity.value?.universityId ?: return
        val departmentId = _selectedDepartment.value?.departmentId ?: return
        val gradeNumber = _grade.value.filter { it.isDigit() }.toIntOrNull() ?: return
        viewModelScope.launch {
            userRepository.updateSchoolInfo(universityId, departmentId, gradeNumber)
            onDone()
        }
    }
}