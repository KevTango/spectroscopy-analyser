package com.example.spectroscopy_app.connection

import retrofit2.Call
import retrofit2.http.GET

interface RequestData {
    @GET("/data")
    fun getSensorData(): Call<SensorJSON>
}