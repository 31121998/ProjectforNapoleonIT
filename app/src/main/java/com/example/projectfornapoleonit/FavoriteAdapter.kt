package com.example.projectfornapoleonit

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun getItemCount(): Int {

        return data.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = inflater.inflate(R.layout.fav_layout, p0, false)
        return ViewHolder(view)
    }
    val data = mutableListOf<User>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.numComicsView).text = data[position].nameComics
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}