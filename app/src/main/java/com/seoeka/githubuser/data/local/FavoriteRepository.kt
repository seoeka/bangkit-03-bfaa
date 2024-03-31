package com.seoeka.githubuser.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.seoeka.githubuser.data.local.room.FavoriteDao
import com.seoeka.githubuser.data.local.room.FavoriteDatabase
import com.seoeka.githubuser.data.local.room.FavoriteUser
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getFavoriteUser(favoriteUser: FavoriteUser) {
        executorService.execute {
            mFavoriteUserDao.getFavoriteUser(favoriteUser)
        }
    }

    fun getFavoriteByUsername(username : String): LiveData<FavoriteUser> = mFavoriteUserDao.getFavoriteUserByUsername(username)

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        executorService.execute {
            mFavoriteUserDao.deleteFavoriteUser(favoriteUser)
        }
    }

    fun getAllFavoriteUser() : LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUser()
}
