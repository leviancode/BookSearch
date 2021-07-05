package com.leviancode.booksearch.ui.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter(value = ["loadImage"])
fun ImageView.loadImage(uri: String?){
    if (uri.isNullOrBlank()) return
    Glide.with(context).load(uri).into(this)
}