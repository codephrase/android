package com.codephrase.android.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(oldImageUrl: String?, newImageUrl: String?) {
    if (oldImageUrl != newImageUrl) {
        if (!newImageUrl.isNullOrEmpty()) {
            Glide.with(context).load(newImageUrl).into(this)
        } else {
            Glide.with(context).clear(this)
            setImageDrawable(null)
        }
    }
}