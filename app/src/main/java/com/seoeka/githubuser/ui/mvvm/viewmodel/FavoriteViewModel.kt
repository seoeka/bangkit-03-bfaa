package com.seoeka.githubuser.ui.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.seoeka.githubuser.data.repository.FavoriteRepository
import com.seoeka.githubuser.data.database.FavoriteUser


class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavUser(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavoriteUser()
}