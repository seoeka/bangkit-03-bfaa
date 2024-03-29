package com.seoeka.githubuser.data.retrofit

import com.seoeka.githubuser.data.response.DetailUserResponse
import com.seoeka.githubuser.data.response.GithubResponse
import com.seoeka.githubuser.data.response.UserItems
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearch(
        @Query("q") username: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(
        @Path("username") username : String
    ): Call<DetailUserResponse>
    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getListFollower(
        @Path("username") username: String
    ): Call<List<UserItems>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getListFollowing(
        @Path("username") username: String
    ): Call<List<UserItems>>
}
