package com.joostleo.linguadailyapp.viewmodel

// RewardedAdManager.kt

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError


/*
* Currently using Googles Test IDs
*
* - ca-app-pub-3940256099942544~3347511713
* - ca-app-pub-3940256099942544/5224354917
* */


object RewardedAdManager {
    private var rewardedAd: RewardedAd? = null
    private var isLoading = false

    fun loadAd(context: Context) {
        if (isLoading || rewardedAd != null) return

        isLoading = true
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            context,
            "ca-app-pub-5606958515134668/3416222558", //  Rewarded Ad Unit ID
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    isLoading = false
                }
            }
        )
    }

    fun showAd(activity: Activity, onReward: () -> Unit) {
        val ad = rewardedAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    loadAd(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    rewardedAd = null
                    loadAd(activity)
                }

                override fun onAdShowedFullScreenContent() {
                    rewardedAd = null
                }
            }

            ad.show(activity) { rewardItem: RewardItem ->
                // User watched the ad
                onReward()
            }
        } else {
            // Optionally show message: "Ad not ready"
            loadAd(activity)
        }
    }
}
