package com.example.arexperiment.domain.memory

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryManager @Inject constructor(
    private val context: Context
) {
    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    private val memoryClass = activityManager.memoryClass
    private val lowMemoryThreshold = (memoryClass * 0.2).toInt() // 20% of available memory

    fun shouldReduceQuality(): Boolean {
        val info = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(info)
        
        return info.availMem <= info.totalMem * 0.2 // Less than 20% memory available
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun getTrimLevel(): Int {
        val info = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(info)
        
        return when {
            info.availMem <= info.totalMem * 0.1 -> ActivityManager.TRIM_MEMORY_COMPLETE
            info.availMem <= info.totalMem * 0.2 -> ActivityManager.TRIM_MEMORY_MODERATE
            info.availMem <= info.totalMem * 0.3 -> ActivityManager.TRIM_MEMORY_BACKGROUND
            else -> ActivityManager.TRIM_MEMORY_RUNNING_MODERATE
        }
    }

    fun cleanup() {
        System.gc()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val trimLevel = getTrimLevel()
            // Notify components to reduce memory usage based on trim level
        }
    }

    fun isLowMemory(): Boolean {
        val info = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(info)
        return info.lowMemory
    }

    fun getAvailableMemory(): Long {
        val info = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(info)
        return info.availMem
    }
}