package com.apolis.workmanagerdemo1.data

import com.google.gson.annotations.SerializedName

data class LiveScore(
    @SerializedName("team1")
    val team1: Team,

    @SerializedName("team2")
    val team2: Team
)
