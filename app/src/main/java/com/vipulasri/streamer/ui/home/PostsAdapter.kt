package com.vipulasri.streamer.ui.home

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.vipulasri.streamer.R
import com.vipulasri.streamer.extensions.inflateView
import com.vipulasri.streamer.extensions.loadImageUrl
import com.vipulasri.streamer.model.Post
import kotlinx.android.synthetic.main.item_post.view.*

class PostsAdapter(var mFeedList: MutableList<Post>) : RecyclerView.Adapter<PostsAdapter.CategoryItemViewHolder>() {

    interface Callbacks {
        fun onPostClick(post: Post)
    }

    private var callbacks: Callbacks? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder(
            inflateView(
                R.layout.item_post,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        getItem(position).run {
            imageUrl.let { holder.image.loadImageUrl(it) }
            holder.name.text = name
            holder.points.text = info

            holder.itemView.setOnClickListener { callbacks?.onPostClick(this) }
        }
    }

    fun setData(list: List<Post>) {
        mFeedList.clear()
        mFeedList.addAll(list)
        notifyDataSetChanged()
    }

    private fun getItem(position: Int) = mFeedList[position]

    override fun getItemCount() = mFeedList.size

    fun setCallbacks(callbacks: Callbacks) {
        this.callbacks = callbacks
    }

    class CategoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.image_post
        val name = itemView.text_post_name
        val points = itemView.text_post_points
    }

}