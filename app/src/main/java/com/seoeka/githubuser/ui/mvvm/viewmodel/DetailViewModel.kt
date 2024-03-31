package com.seoeka.githubuser.ui.mvvm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seoeka.githubuser.data.repository.FavoriteRepository
import com.seoeka.githubuser.data.database.FavoriteUser
import com.seoeka.githubuser.data.response.DetailUserResponse
import com.seoeka.githubuser.data.response.UserItems
import com.seoeka.githubuser.data.retrofit.ApiConfig
import com.seoeka.githubuser.ui.view.DetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _userDetail = MutableLiveData<DetailUserResponse>()
    val userDetail: LiveData<DetailUserResponse> = _userDetail

    private val _userFollower = MutableLiveData<List<UserItems>>()
    val userFollower: LiveData<List<UserItems>> = _userFollower

    private val _userFollowing = MutableLiveData<List<UserItems>>()
    val userFollowing: LiveData<List<UserItems>> = _userFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingFollow = MutableLiveData<Boolean>()
    val isLoadingFollow: LiveData<Boolean> = _isLoadingFollow

    private val mFavoriteUserRepository: FavoriteRepository = FavoriteRepository(application)

    init {
        getUser(DetailActivity.username)
        getUserFollower(DetailActivity.username)
        getUserFollowing(DetailActivity.username)
    }

    fun getFavoriteUser(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.getFavoriteUser(favoriteUser)
    }

    fun getFavoriteUser(username: String): LiveData<FavoriteUser> = mFavoriteUserRepository.getFavoriteByUsername(username)

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.deleteFavoriteUser(favoriteUser)
    }

    fun getUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserFollower(username: String) {
        _isLoadingFollow.value = true
        val client = ApiConfig.getApiService().getListFollower(username)
        client.enqueue(object : Callback<List<UserItems>> {
            override fun onResponse(
                call: Call<List<UserItems>>,
                response: Response<List<UserItems>>
            ) {
                _isLoadingFollow.value = false
                if (response.isSuccessful) {
                    _userFollower.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<UserItems>>, t: Throwable) {
                _isLoadingFollow.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserFollowing(username: String) {
        _isLoadingFollow.value = true
        val client = ApiConfig.getApiService().getListFollowing(username)
        client.enqueue(object : Callback<List<UserItems>> {
            override fun onResponse(
                call: Call<List<UserItems>>,
                response: Response<List<UserItems>>
            ) {
                _isLoadingFollow.value = false
                if (response.isSuccessful) {
                    _userFollowing.value = response.body()
                    Log.d("response", _userFollowing.value.toString())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<UserItems>>, t: Throwable) {
                _isLoadingFollow.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
    companion object{
        private const val TAG = "MainViewModel"
    }
}