package com.alexander.githubapp.data.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("name")
	val name: String,
)
