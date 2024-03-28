package com.seoeka.githubuser.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seoeka.githubuser.R
import com.seoeka.githubuser.data.response.UserItems

class ListUserAdapter (private val listUsers: List<UserItems>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.list_user_row, viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvName.text = listUsers[position].login
        Glide.with(viewHolder.itemView.context)
            .load(listUsers[position].avatarUrl)
            .into(viewHolder.imgPhoto)
    }
    override fun getItemCount() = listUsers.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.user_img)
        val tvName: TextView = itemView.findViewById(R.id.user_name)
    }
}