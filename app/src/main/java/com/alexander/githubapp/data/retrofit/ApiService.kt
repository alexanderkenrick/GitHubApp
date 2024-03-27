package com.alexander.githubapp.data.retrofit

import com.alexander.githubapp.data.response.DetailUserResponse
import com.alexander.githubapp.data.response.GitHubResponse
import com.alexander.githubapp.data.response.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") userId: String
    ): Call<GitHubResponse>

    @GET("users/{username}")
    fun getDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<User>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<User>>
}