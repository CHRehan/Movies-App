package com.example.movies.ui.moviedetail

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.movies.data.entities.flickrPhoto.Photo
import com.example.movies.databinding.ItemPictureBinding
import com.example.movies.utils.Helpers.getFlickrImageURL

class MoviePicturesAdapter() : RecyclerView.Adapter<PictureViewHolder>() {


    private val pictures = ArrayList<Photo>()

    fun setItems(items: List<Photo>?) {
        this.pictures.clear()
        items?.let {
            this.pictures.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val binding: ItemPictureBinding =
            ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PictureViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = pictures.size

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(pictures[position])


    }


}

class PictureViewHolder(
    private val itemBinding: ItemPictureBinding,
    private val context: Context
) : RecyclerView.ViewHolder(itemBinding.root) {


    fun bind(item: Photo) {
        context.let {
            Glide.with(it)
                .load(getFlickrImageURL(context, item))
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {


                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemBinding.progressBar.visibility = GONE
                        itemBinding.image.visibility = VISIBLE
                        return false
                    }
                }).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))

                .into(itemBinding.image)


        }

    }


}

