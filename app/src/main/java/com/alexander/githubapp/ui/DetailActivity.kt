package com.alexander.githubapp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.widget.ViewPager2
import com.alexander.githubapp.R
import com.alexander.githubapp.data.response.DetailUserResponse
import com.alexander.githubapp.data.response.User
import com.alexander.githubapp.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val username = intent.getStringExtra(EXTRA_USERNAME)
        detailViewModel.findUserDetail(username.toString())

        detailViewModel.user.observe(this) { username ->
            setUserDetail(username)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username.toString())
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setUserDetail(user: DetailUserResponse) {
        binding.txtName.text = user.name
        binding.txtUsername.text = user.login
        binding.txtFollowers.text = "${user.followers} Followers"
        binding.txtFollowing.text = "${user.following} Following"
        Glide.with(this)
            .load(user.avatarUrl)
            .into(binding.userImg)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.userImg.visibility = View.INVISIBLE
            binding.txtUsername.visibility = View.INVISIBLE
            binding.txtName.visibility = View.INVISIBLE
            binding.txtFollowing.visibility = View.INVISIBLE
            binding.txtFollowers.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.userImg.visibility = View.VISIBLE
            binding.txtUsername.visibility = View.VISIBLE
            binding.txtName.visibility = View.VISIBLE
            binding.txtFollowing.visibility = View.VISIBLE
            binding.txtFollowers.visibility = View.VISIBLE
        }
    }


}