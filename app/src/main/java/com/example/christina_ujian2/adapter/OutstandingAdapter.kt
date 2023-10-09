package com.example.christina_ujian2.adapter

import android.R.attr.data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.androidstarting.model.DataOutstanding
import com.example.christina_ujian2.R
import com.example.christina_ujian2.callback.OnItemClick
import com.example.christina_ujian2.callback.OnItemLongClick
import java.text.NumberFormat
import java.util.Locale


class OutstandingAdapter (val outstandingList: List<DataOutstanding?>?, val itemClickListener: OnItemClick<DataOutstanding>, val itemLongClickListener: OnItemLongClick<DataOutstanding>, val deleteItemClickListener: OnItemClick<DataOutstanding>) : RecyclerView.Adapter<OutstandingAdapter.OutstandingViewHolder>() {

    inner class OutstandingViewHolder(view: View) : RecyclerView.ViewHolder(view){

        // mapping layout dengan findViewById
        val tvNama = view.findViewById<TextView>(R.id.tvNama)
        val tvAlamat = view.findViewById<TextView>(R.id.tvAlamat)
        val tvOutstanding = view.findViewById<TextView>(R.id.tvOutstanding)
        val ivRemove = view.findViewById<ImageView>(R.id.ivRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutstandingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_outstanding,parent,false)
        return OutstandingViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (outstandingList != null) {
            return outstandingList.size
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: OutstandingViewHolder, position: Int) {
        if (outstandingList != null) {
            holder.tvNama.text = outstandingList[position]!!.nama
            holder.tvAlamat.text = outstandingList[position]!!.alamat
            holder.tvOutstanding.text = "Rp ${NumberFormat.getInstance(Locale("id", "ID")).format(outstandingList[position]!!.outstanding!!.toInt())}"
            holder.itemView.setOnClickListener(View.OnClickListener {
                itemClickListener.onItemClick(outstandingList[position]!!)
            })

            holder.itemView.setOnLongClickListener ({
                itemLongClickListener.onItemLongClick(outstandingList[position]!!)
                true
            })

            holder.ivRemove.setOnClickListener({
                deleteItemClickListener.onItemClick(outstandingList[position]!!)
            })

            holder.itemView.setOnClickListener({
                itemClickListener.onItemClick(outstandingList[position]!!)
            })
        }
    }

    fun getData(): List<DataOutstanding?>? {
        return outstandingList
    }


}