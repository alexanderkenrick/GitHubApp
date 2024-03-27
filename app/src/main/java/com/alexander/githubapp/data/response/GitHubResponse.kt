package com.alexander.githubapp.data.response

import com.google.gson.annotations.SerializedName

data class GitHubResponse(
	@field:SerializedName("items")
	val listUser: List<User>
)

data class User(
	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("followers_url")
	val followersUrl: String,
)
