package com.apolis.workmanagerdemo1

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import kotlin.math.log

class InboxFetcherWork(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // user id as input data to this doMethod
        val userId = inputData.getInt("user_id", -1)
        if(userId == -1) {
            val data = Data.Builder()
                .putString("message", "User id is missing in the request.")
                .build()

            return Result.failure(data)
        }

        for(i in 1..10) {
            delay(1000)
            val progressData = Data.Builder()
                .putInt("total_messages", 10)
                .putInt("messages_loaded", i)
                .build()
            setProgress(progressData)
        }

        val data = Data.Builder()
            .putString("message", "Inbox has synced with server successfully")
            .build()

        return Result.success(data)
    }

}