package com.apolis.workmanagerdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.work.*

class MainActivity : AppCompatActivity() {
    lateinit var btnLoadMsgs: Button
    lateinit var workManager: WorkManager
    lateinit var tvMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLoadMsgs = findViewById(R.id.btn_load_messages)
        tvMsg = findViewById(R.id.tv_msg)

        btnLoadMsgs.setOnClickListener {
            startLoadMessagesWork()
        }
    }

    private fun startLoadMessagesWork() {


        val data = Data.Builder()
            .putInt("user_id", 123)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val inboxWorkRequest = OneTimeWorkRequestBuilder<InboxFetcherWork>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()



        workManager = WorkManager.getInstance(this)

        workManager.enqueue(inboxWorkRequest)

        workManager.getWorkInfoByIdLiveData(inboxWorkRequest.id).observe(this) {
            info: WorkInfo ->
            Log.d("MainActivity", "startLoadMessagesWork: ${info.state}")

            when(info.state) {
                WorkInfo.State.SUCCEEDED -> {
                    val message = info.outputData.getString("message")
                    tvMsg.text = message
                    Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
                }
                WorkInfo.State.FAILED -> {
                    val message = info.outputData.getString("message")
                    tvMsg.text = message
                    Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
                }
                WorkInfo.State.RUNNING -> {
                    val totalMessages = info.progress.getInt("total_messages", -1)
                    val messages_loaded = info.progress.getInt("messages_loaded", -1)
                    if(totalMessages != -1 && messages_loaded != -1) {
                        tvMsg.setText("$messages_loaded / $totalMessages loaded")
                    }
                }
                else -> {

                }
            }
        }

    }
}