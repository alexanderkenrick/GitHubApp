package com.alexander.githubapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.alexander.githubapp.R
import com.alexander.githubapp.data.response.DetailUserResponse
import com.alexander.githubapp.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    private var isFavorite = false
    private var userUrl = ""

    companion object {
        const val EXTRA_USERNAME = "extra_username"

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

        detailViewModel.user.observe(this) { user ->
            setUserDetail(user)
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

        lifecycleScope.launch(Dispatchers.Default) {
            isFavorite = detailViewModel.getFavoriteStatus(username ?: "")
            if (isFavorite) {
                binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }

        binding.btnFavorite.setOnClickListener() {
            lifecycleScope.launch(Dispatchers.Default) {
                isFavorite = if (isFavorite) {
                    detailViewModel.deleteFavorite(username ?: "")
                    binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                    workerThread("Removed from favorite")
                    false
                } else {
                    detailViewModel.addFavorite(username ?: "", userUrl)
                    binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
                    workerThread("Added to favorite")
                    true
                }
            }
        }
    }

    @WorkerThread
    fun workerThread(message: String) {
        this.runOnUiThread {
            Toast.makeText(this@DetailActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUserDetail(user: DetailUserResponse) {
        binding.txtName.text = user.name
        binding.txtUsername.text = user.login
        binding.txtFollowers.text = "${user.followers} Followers"
        binding.txtFollowing.text = "${user.following} Following"
        userUrl = user.avatarUrl
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