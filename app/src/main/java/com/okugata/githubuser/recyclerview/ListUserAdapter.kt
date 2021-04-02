package com.okugata.githubuser.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.okugata.githubuser.R
import com.okugata.githubuser.databinding.ItemUserBinding
import com.okugata.githubuser.model.User

class ListUserAdapter (private val listUser: ArrayList<User>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user,
            parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]

        holder.bind(user)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(user)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)
        internal fun bind(user: User) {
            if (user.avatarUrl.isNotEmpty()){
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .into(binding.imgItemPhoto)
            }
            else {
                binding.imgItemPhoto.setImageResource(user.avatar)
            }
            binding.tvItemUsername.text = user.username
            binding.tvItemName.text = user.name
        }
    }
}