package com.alexander.githubapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.alexander.githubapp.data.database.FavoriteDao
import com.alexander.githubapp.data.database.FavoriteUser
import com.alexander.githubapp.data.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorite(): LiveData<List<FavoriteUser>> = mFavoriteDao.getAllFavorite()

    fun getFavoriteUserStatus(username: String): FavoriteUser =
        mFavoriteDao.getFavoriteUserStatus(username)

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.delete(favoriteUser) }
    }
}