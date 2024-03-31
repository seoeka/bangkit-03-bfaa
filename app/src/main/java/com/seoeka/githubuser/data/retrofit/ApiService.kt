package com.seoeka.githubuser.data.retrofit

import com.seoeka.githubuser.BuildConfig
import com.seoeka.githubuser.data.response.DetailUserResponse
import com.seoeka.githubuser.data.response.GithubResponse
import com.seoeka.githubuser.data.response.UserItems
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token $API_KEY")
    fun getSearch(
        @Query("q") username: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $API_KEY")
    fun getDetailUser(
        @Path("username") username : String
    ): Call<DetailUserResponse>
    @GET("users/{username}/followers")
    @Headers("Authorization: token $API_KEY")
    fun getListFollower(
        @Path("username") username: String
    ): Call<List<UserItems>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $API_KEY")
    fun getListFollowing(
        @Path("username") username: String
    ): Call<List<UserItems>>

    companion object {
        const val API_KEY: String = BuildConfig.API_KEY
    }
}
