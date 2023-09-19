package com.example.lab2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class RecipeAdapter(datalist: List<DataItem>, listener: Listener) : RecyclerView.Adapter<RecipeAdapter.CourseViewHolder>() {

    private var dataList: List<DataItem> = datalist
    private var listener = listener
    //private var dataList: List<String> = datalist

    override fun getItemCount(): Int {
        return this.dataList.size
    }

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageItem: ImageView = itemView.findViewById(R.id.imageView)
        val textItem: TextView = itemView.findViewById(R.id.textView)

        fun listenOnClick(dataItem: DataItem, position: Int, listener: Listener) {
            itemView.setOnClickListener {
                listener.onCLick(dataItem);
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return CourseViewHolder(layoutInflater.inflate(R.layout.category_item, parent, false))
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        Picasso.get()
            .load(dataList[position].image)
            .into(holder.imageItem)
        holder.textItem.text = dataList[position].title
        holder.listenOnClick(dataList[position], position, listener)
    }

    interface Listener {
        fun onCLick(dataItem: DataItem)
    }
}