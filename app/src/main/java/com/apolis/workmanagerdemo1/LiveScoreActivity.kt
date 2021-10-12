package com.apolis.workmanagerdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.work.*
import com.apolis.workmanagerdemo1.bgworkers.LiveScoreWorker
import com.apolis.workmanagerdemo1.databinding.ActivityLiveScoreBinding
import java.util.concurrent.TimeUnit

class LiveScoreActivity : AppCompatActivity() {

    lateinit var binding: ActivityLiveScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startLoadingLiveScores()
    }

    private fun startLoadingLiveScores() {

        val timeInterval: Long = 5

        val inputData = Data.Builder()
            .putString("match_id", "team_a_vs_team_b")
            .build()

        val liveScoreRequest =
            PeriodicWorkRequestBuilder<LiveScoreWorker>(timeInterval, TimeUnit.SECONDS)
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                )
                .setInitialDelay(5, TimeUnit.SECONDS)
                .setInputData(inputData)
                .build()

        val workManager = WorkManager.getInstance(this)

        val operation = workManager.enqueueUniquePeriodicWork("LiveScoreLoader",
            ExistingPeriodicWorkPolicy.KEEP,
            liveScoreRequest)


        workManager.getWorkInfoByIdLiveData(liveScoreRequest.id).observeForever { info: WorkInfo ->
            Log.d("LiveScore", "State = ${info.state} ")

            val firstTeamName = info.progress.getString("team1")
            val firstTeamGoals = info.progress.getInt("goals1", 0)

            val secondTeamName = info.progress.getString("team2")
            val secondTeamGoals = info.progress.getInt("goals2", 0)

                binding.tvScore1.text =
                    "$firstTeamName [ $firstTeamGoals ] - $secondTeamName [ $secondTeamGoals ]"



        }
    }


}