package com.seoeka.githubuser.ui.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.seoeka.githubuser.data.response.GithubResponse
import com.seoeka.githubuser.data.response.UserItems
import com.seoeka.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUsers = MutableLiveData<List<UserItems?>?>()
    val listUsers: LiveData<List<UserItems?>?> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    companion object{
        private const val TAG = "MainViewModel"
        private const val DEFAULT_USER_ID = "eka"
    }
    init {
        getUsers(DEFAULT_USER_ID)
    }
    fun getUsers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearch(username)
        client.enqueue(object : Callback<GithubResponse>
         {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUsers.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}