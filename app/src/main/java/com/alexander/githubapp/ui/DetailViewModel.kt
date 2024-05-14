package com.alexander.githubapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexander.githubapp.data.database.FavoriteUser
import com.alexander.githubapp.data.repository.FavoriteRepository
import com.alexander.githubapp.data.response.DetailUserResponse
import com.alexander.githubapp.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    private val _user = MutableLiveData<DetailUserResponse>()
    val user: LiveData<DetailUserResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    internal fun findUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetail(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _user.value = response.body()
                    }
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

    fun getFavoriteStatus(username: String): Boolean {
        mFavoriteRepository.getFavoriteUserStatus(username) ?: return false
        return true
    }

    fun deleteFavorite(username: String) {
        val favUser = mFavoriteRepository.getFavoriteUserStatus(username)
        mFavoriteRepository.delete(favUser)
    }

    suspend fun addFavorite(username: String, avatarUrl: String) = withContext(Dispatchers.IO) {
        val favUser = FavoriteUser(username, avatarUrl)
        mFavoriteRepository.insert(favUser)
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}