package com.aviax.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aviax.game.databinding.ListItemBinding

class ScoreAdapter(val data: MutableList<String>): RecyclerView.Adapter<ScoreAdapter.Companion.MyHolder>() {

    init {
        while(data.size<5) data.add("-")
    }

    companion object {
        class MyHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return  MyHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
       holder.binding.textView6.text = data[position]
        holder.binding.textView7.text = "${position+1}"
    }

    override fun getItemCount(): Int {
        return data.size
    }
}