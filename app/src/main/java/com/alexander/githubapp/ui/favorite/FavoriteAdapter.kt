package com.alexander.githubapp.ui.favorite

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexander.githubapp.data.database.FavoriteUser
import com.alexander.githubapp.databinding.ItemRowUserBinding
import com.alexander.githubapp.ui.DetailActivity
import com.bumptech.glide.Glide

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listFavorite = ArrayList<FavoriteUser>()

    fun setListFavorite(listFavorite: List<FavoriteUser>) {
        val diffCallBack = FavoriteCallBack(this.listFavorite, listFavorite)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorite)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position], holder)
        val user = listFavorite[position]

        holder.itemView.setOnClickListener {
            val activity = holder.itemView.context as Activity
            val moveUsername = Intent(activity, DetailActivity::class.java)
            moveUsername.putExtra(DetailActivity.EXTRA_USERNAME, user.username)
            activity.startActivity(moveUsername)

        }

    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    inner class FavoriteViewHolder(private val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUser, holder: FavoriteViewHolder) {
            with(binding) {
                textView.text = favoriteUser.username
                Glide.with(holder.itemView.context)
                    .load(favoriteUser.avatarUrl)
                    .into(holder.binding.userImg)
            }
        }
    }
}