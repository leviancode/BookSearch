package com.leviancode.booksearch.ui.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.leviancode.booksearch.R

@BindingAdapter(value = ["loadImage"])
fun ImageView.loadImage(uri: String?){
    if (uri.isNullOrBlank()) {
        Glide.with(context).load(R.drawable.image_placeholder).into(this)
    } else {
        Glide.with(context).load(uri).into(this)
    }
}