package com.example.movies.utils

import android.content.Context
import com.example.movies.R
import com.example.movies.data.entities.flickrPhoto.Photo
import java.io.IOException
import java.nio.charset.StandardCharsets

object Helpers {
    @JvmStatic
    fun loadJSONFromAsset(context: Context, fileName: String?): String? {
        try {
            val inputStream = context.assets.open(fileName!!)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            return String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return null
    }

    @JvmStatic
    fun getFlickrImageURL(context: Context, photo: Photo): String =
        context.getString(R.string.image_url,photo.server, photo.id, photo.secret)


}