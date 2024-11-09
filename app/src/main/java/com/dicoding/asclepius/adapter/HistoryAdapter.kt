package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.databinding.ItemViewHistoryBinding
import com.dicoding.asclepius.local.LocalResponse
import com.dicoding.asclepius.view.ResultActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class HistoryAdapter : ListAdapter<LocalResponse, HistoryAdapter.HistoryViewHolder>(Diff_Callback) {
    inner class HistoryViewHolder (private val binding: ItemViewHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(localResponse: LocalResponse) {
            binding.apply {
                tvLabel.text = localResponse.label
                tvScore.text = "Score: " + NumberFormat.getPercentInstance().format(localResponse.score)
                tvTimestamp.text = formatTimestamp(localResponse.timestamp)
                Glide.with(itemView.context)
                    .load(localResponse.imageUri)
                    .into(ivImage)
            }

        }

        private fun formatTimestamp(timestamp: Long): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val instant = Instant.ofEpochMilli(timestamp)
                val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
                    .withZone(ZoneId.systemDefault())
                formatter.format(instant)
            } else {
                val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
                sdf.format(Date(timestamp))
            }
        }

    }

    companion object{
        val Diff_Callback = object : DiffUtil.ItemCallback<LocalResponse>() {
            override fun areItemsTheSame(oldItem: LocalResponse, newItem: LocalResponse): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: LocalResponse, newItem: LocalResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemViewHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ResultActivity::class.java)
            intent.putExtra(ResultActivity.EXTRA_RESULT, getItem(position))
            holder.itemView.context.startActivity(intent)
        }
    }
}