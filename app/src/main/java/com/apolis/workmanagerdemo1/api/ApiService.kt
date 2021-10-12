package com.apolis.workmanagerdemo1.api

import com.apolis.workmanagerdemo1.data.LiveScore
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("live_score.php")
    suspend fun getLiveScore(@Field("match_id") matchId: String): Response<LiveScore>

}