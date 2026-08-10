package com.umc.catchandroid.domain.model

data class Faq(
    val faqId: Long,
    val category: String,
    val question: String,
    val answer: String
)