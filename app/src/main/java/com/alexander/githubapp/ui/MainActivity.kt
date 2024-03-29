package com.alexander.githubapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexander.githubapp.R
import com.alexander.githubapp.data.response.User
import com.alexander.githubapp.databinding.ActivityMainBinding
import com.alexander.githubapp.preferences.SettingPreferences
import com.alexander.githubapp.preferences.dataStore
import com.alexander.githubapp.ui.favorite.FavoriteActivity
import com.alexander.githubapp.ui.preferences.ThemeActivity
import com.alexander.githubapp.ui.preferences.ThemeViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val themeViewModel = ViewModelProvider(this, ViewModelFactory(this.application, pref)).get(
            ThemeViewModel::class.java
        )

        themeViewModel.getThemeSettings().observe(this) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.listUser.observe(this) { listUser ->
            setUserData(listUser)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)

            searchBar.inflateMenu(R.menu.option_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_favorite -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.menu_theme -> {
                        val intent = Intent(this@MainActivity, ThemeActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false

                }
            }

            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                val usernameSearched = searchView.text.toString()
                mainViewModel.findUser(usernameSearched)
                searchView.hide()

                false
            }
        }
    }

    private fun setUserData(listUser: List<User>) {
        val adapter = UserAdapter()
        adapter.submitList(listUser)
        if (listUser.isEmpty()) {
            with(binding.txtNotExist) {
                visibility = View.VISIBLE
                "Data not available".also { text = it }
            }
        } else {
            binding.txtNotExist.visibility = View.GONE
        }
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}