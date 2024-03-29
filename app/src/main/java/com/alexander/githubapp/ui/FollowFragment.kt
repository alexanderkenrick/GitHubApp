package com.alexander.githubapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexander.githubapp.data.response.User
import com.alexander.githubapp.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private var position: Int = 1
    private var username: String? = ""
    private val viewModel by viewModels<FollowViewModel>()

    companion object {
        const val ARG_POSITION = "1"
        const val ARG_USERNAME = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        viewModel.findFollow(username.toString(), position)
        viewModel.listUser.observe(viewLifecycleOwner) { listUser ->
            setUserData(listUser)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(view.context)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(view.context, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)
    }

    @SuppressLint("SetTextI18n")
    private fun setUserData(listUser: List<User>) {
        val adapter = UserAdapter()
        if (listUser.isEmpty()) {
            with(binding.txtNotExist) {
                visibility = View.VISIBLE
                text = "Data Not Available"
            }
        } else {
            binding.txtNotExist.visibility = View.GONE
        }
        adapter.submitList(listUser)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

}