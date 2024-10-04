package com.azamovhudstc.tashkentmetro.ui.screens.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.tashkentmetro.data.model.station.Station
import com.azamovhudstc.tashkentmetro.databinding.SearchViewItemBinding

class SearchViewAdapter : RecyclerView.Adapter<SearchViewAdapter.SearchViewVh>(
) {
    var list = ArrayList<Station>()

    inner class SearchViewVh(val binding: SearchViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(station: Station) {
            binding.apply {
                metroTitle.text=station.name
                metroType.text= station.state.name
                metroLine.text =station.line.name

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewVh {

        return SearchViewVh(
            SearchViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun submitList(newList: ArrayList<Station>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SearchViewVh, position: Int) {

        holder.onBind(list.get(position))
    }
}