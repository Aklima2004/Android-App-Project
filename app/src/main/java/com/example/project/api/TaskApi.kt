package com.example.project.api

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import com.example.project.model.Task

interface TaskApi {

    @GET("tasks")
    suspend fun fetchTasks(): List<Task>

    @POST("tasks")
    suspend fun addTask(@Body task: Task)
}
