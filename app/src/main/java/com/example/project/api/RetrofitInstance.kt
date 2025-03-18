package com.example.project.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit: Retrofit by lazy {
        val gson = GsonBuilder().setLenient().create()
        Retrofit.Builder()
            .baseUrl("https://yourapi.com/tasks/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val api: TaskApi by lazy {
        retrofit.create(TaskApi::class.java)
    }
}
