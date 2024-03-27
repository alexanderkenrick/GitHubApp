package com.alexander.githubapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexander.githubapp.data.response.User
import com.alexander.githubapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

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

            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
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

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
}