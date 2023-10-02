package com.mbanna.bookshelf.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {


    fun convertUnixTimestampStringToFormattedDate(unixTimestampString: Int): String {
        try {
            val unixTimestamp = unixTimestampString.toLong()
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val date = Date(unixTimestamp * 1000L) // Convert Unix timestamp to milliseconds
            return sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun loadImage(context: Context, imageUrl: String?, imageView: ImageView) {
        val requestOptions = RequestOptions()
            .transforms(CenterCrop(), RoundedCorners(16))

        Glide.with(context)
            .load(imageUrl)
            .apply(requestOptions)
            .into(imageView)
    }

}


