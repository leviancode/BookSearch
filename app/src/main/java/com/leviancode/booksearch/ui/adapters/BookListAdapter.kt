package com.leviancode.booksearch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.booksearch.core.models.Book
import com.leviancode.booksearch.databinding.ListItemBookBinding

class BookListAdapter : ListAdapter<Book, BookListAdapter.BookViewHolder>(BooksDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            ListItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BookViewHolder(private val binding: ListItemBookBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Book){
            binding.model = item
            binding.executePendingBindings()
        }
    }

    class BooksDiffUtil : DiffUtil.ItemCallback<Book>(){
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }

    }
}