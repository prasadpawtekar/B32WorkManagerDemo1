package com.apolis.workmanagerdemo1.data

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("name")
    val name: String,

    @SerializedName("goals")
    val goals: Int
)
