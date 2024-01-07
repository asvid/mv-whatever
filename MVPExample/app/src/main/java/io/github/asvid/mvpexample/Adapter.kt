package io.github.asvid.mvpexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    private var data: List<Int>,
    private val onClickListener: (Int) -> Unit
) : RecyclerView.Adapter<Adapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTextView: TextView = itemView.findViewById(R.id.itemTextView)
        var itemButton: Button = itemView.findViewById(R.id.itemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = data[position]
        holder.itemTextView.text = item.toString()
        holder.itemButton.setOnClickListener { onClickListener(item) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(data: List<Int>) {
        this.data = data
        notifyDataSetChanged()
    }
}