package com.example.spectroscopy_app.connection

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET


private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl("http://" + Constant.ipAddress)
    .build()

interface Network {
    @GET("photos")
    fun getPhotos(): String
}

object MarsApi {
    val retrofitService : Network by lazy {
        retrofit.create(Network::class.java) }
}
