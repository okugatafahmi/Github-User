package com.okugata.githubuserfavorite.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.okugata.githubuserfavorite.R
import com.okugata.githubuserfavorite.databinding.ItemUserBinding
import com.okugata.githubuserfavorite.model.User

class ListUserAdapter :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private val listUser = ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setListUser(items: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user,
            parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]

        holder.bind(user)

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(user)
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