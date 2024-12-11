package com.example.smartpaddy.presentation.history.adapter

<<<<<<< HEAD
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.smartpaddy.data.response.DataResponse
=======
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.smartpaddy.data.response.DataResponse
import com.example.smartpaddy.presentation.historyDetail.HistoryDetailActivity
>>>>>>> refs/heads/md-bella
import com.example.smartpaddy.presentation.home.adapter.HistoryViewHolder
import com.example.smartpaddy.presentation.home.adapter.HistoryViewHolder.Companion

class HistoryAdapter : Adapter<HistoryViewHolder>() {

  private var historyList: List<DataResponse> = listOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
    return Companion.create(parent)
  }

  override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
    val imageUrl = historyList[position].imageUrl
    val historyItem = historyList[position].result

    if (imageUrl != null) {
      holder.bind(historyItem, imageUrl)
    }
<<<<<<< HEAD
=======

    val postId = historyList[position].predictId

    holder.itemView.setOnClickListener {
      val intentToHistoryDetail = Intent(holder.itemView.context, HistoryDetailActivity::class.java)
      intentToHistoryDetail.putExtra("imageUrl", imageUrl)
      intentToHistoryDetail.putExtra("postId", postId)
      Log.e("bella", "ini postId $postId")
      holder.itemView.context.startActivity(intentToHistoryDetail)
    }
>>>>>>> refs/heads/md-bella
  }

  override fun getItemCount(): Int {
    return historyList.size
  }

  fun setHistoryList(newHistoryList: List<DataResponse>) {
    if (newHistoryList != historyList) {
      historyList = newHistoryList
      notifyDataSetChanged()
    }
  }
}