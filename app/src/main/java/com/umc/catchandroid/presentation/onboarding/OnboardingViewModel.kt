package com.umc.catchandroid.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun saveUniversity(university: String) {
        viewModelScope.launch {
            userRepository.updateOnboardingInfo(university = university)
        }
    }

    fun saveProfile(department: String, grade: Int) {
        viewModelScope.launch {
            userRepository.updateOnboardingInfo(department = department, grade = grade)
        }
    }
}