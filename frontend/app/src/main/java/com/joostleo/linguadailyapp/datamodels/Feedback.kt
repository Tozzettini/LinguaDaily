package com.joostleo.linguadailyapp.datamodels

data class Feedback(
    val feedback: String,
    val appVersion: String? = null,
    val platform: String? = null,
    val deviceModel: String? = null,
    val userId: Long? = null,
    val osVersion: String? = null,
    val feedbackType: String? = null
)
