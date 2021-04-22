package com.ragu.flickerimagesearch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ragu.flickerimagesearch.R
import com.ragu.flickerimagesearch.model.PhotoResponse
import kotlinx.android.synthetic.main.content_main.view.*

class MainAdapter(private val image: ArrayList<PhotoResponse>) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(image: PhotoResponse) {
            itemView.apply {
                Glide.with(imageViewAvatar.context)
                    .load("https://farm${image.farm}.static.flickr.com/${image.server}/${image.id}_${image.secret}.jpg").dontAnimate()
                    .into(imageViewAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.content_main, parent, false))

    override fun getItemCount(): Int = image.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(image[position])
    }

    fun addUsers(users: List<PhotoResponse>) {
        this.image.apply {
           clear()
            addAll(users)
        }

    }
}