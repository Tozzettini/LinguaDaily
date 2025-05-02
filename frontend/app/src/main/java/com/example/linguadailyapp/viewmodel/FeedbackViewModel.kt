package com.example.linguadailyapp.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.linguadailyapp.datamodels.Feedback
import com.example.linguadailyapp.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import android.os.Build
import androidx.lifecycle.AndroidViewModel

class FeedbackViewModel(private val application: Application) : AndroidViewModel(application) {

    fun saveFeedback(feedbackMessage: String, feedbackType : String? = null) {
        viewModelScope.launch {
            try {
                val feedback = Feedback(
                    feedback = feedbackMessage,
                    appVersion = getAppVersion(),
                    platform = "Android",
//                    deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}",
                    osVersion = Build.VERSION.RELEASE,
                    feedbackType = feedbackType
                )
                RetrofitClient.apiService.saveFeedback(feedback)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getAppVersion(): String {
        return try {
            val packageInfo = application.packageManager.getPackageInfo(application.packageName, 0)
            packageInfo.versionName ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }
    }

}