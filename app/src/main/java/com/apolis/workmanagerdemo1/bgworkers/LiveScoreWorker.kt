package com.apolis.workmanagerdemo1.bgworkers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.apolis.workmanagerdemo1.api.ApiClient

class LiveScoreWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        try {
            val matchId = inputData.getString("match_id")?:""

            val response = ApiClient.apiService.getLiveScore(matchId)

            if(response.isSuccessful) {
                response.body()?.let {
                    val data = Data.Builder()
                        .putString("team1", it.team1.name)
                        .putInt("goals1", it.team1.goals)
                        .putString("team2", it.team2.name)
                        .putInt("goals2", it.team2.goals)
                        .build()
                    setProgressAsync(data)

                    return Result.retry()
                }
            } else {
                val data = Data.Builder()
                    .putString("error", "Error while loading score. Error code: ${response.code()}")
                    .build()

                return Result.failure(data)
            }
        } catch (e: Exception) {
            val failureData = Data.Builder()
                .putString("error", e.toString())
                .build()
            return Result.failure(failureData)
        }
        return Result.retry()
    }
}