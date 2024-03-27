package com.alexander.githubapp.ui

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexander.githubapp.data.response.User
import com.alexander.githubapp.databinding.ItemRowUserBinding
import com.bumptech.glide.Glide

class UserAdapter : ListAdapter<User, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user, holder)

        holder.itemView.setOnClickListener {
            val activity = holder.itemView.context as Activity
            val moveUsername = Intent(activity, DetailActivity::class.java)
            moveUsername.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
            activity.startActivity(moveUsername)

        }
    }

    class MyViewHolder(val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, holder: MyViewHolder) {
            binding.textView.text = user.login
            Glide.with(holder.itemView.context)
                .load(user.avatarUrl)
                .into(holder.binding.userImg)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}