package com.seoeka.githubuser.data.retrofit

import com.seoeka.githubuser.data.response.DetailUserResponse
import com.seoeka.githubuser.data.response.GithubResponse
import com.seoeka.githubuser.data.response.ListFollowResponseItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getSearch(
        @Query("q") username: String
    ): Call<GithubResponse>
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username : String
    ): Call<DetailUserResponse>
    @GET("users/{username}/followers")
    fun getListFollower(
        @Path("username") username: String
    ): Call<List<ListFollowResponseItem>>
    @GET("users/{username}/following")
    fun getListFollowing(
        @Path("username") username: String
    ): Call<List<ListFollowResponseItem>>
}
