package com.dicoding.asclepius.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ItemViewNewsBinding
import com.dicoding.asclepius.online.NewsItem

class NewsAdapter : ListAdapter<NewsItem, NewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {
    inner class NewsViewHolder (private val binding: ItemViewNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(newsItem: NewsItem) {
            binding.apply {
                tvTitle.text = newsItem.title
                tvLink.apply {
                    text = "Baca Selengkapnya"
                    visibility = if (newsItem.uri != null) View.VISIBLE else View.GONE
                    setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.uri))
                        context.startActivity(intent)
                    }
                }
                Glide.with(itemView.context)
                    .load(newsItem.imgUrl)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(imgNews)
            }
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemViewNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = getItem(position)
        holder.bind(newsItem)
    }
}