package com.azamovhudstc.tashkentmetro.ui.screens.map

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.tashkentmetro.data.model.station.Line
import com.azamovhudstc.tashkentmetro.data.model.station.Station
import com.azamovhudstc.tashkentmetro.data.model.station.StationState
import com.azamovhudstc.tashkentmetro.databinding.SearchViewItemBinding

class SearchViewAdapter(private val onItemClickListener: (Station) -> Unit) : RecyclerView.Adapter<SearchViewAdapter.SearchViewVh>(
) {
    var list = ArrayList<Station>()

    inner class SearchViewVh(val binding: SearchViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(station: Station) {
            binding.apply {
                popularStation.text = station.name
                stationPosition.text = station.state.name
                gradientView.background = drawGradient(getLineColor(station.line))
                imageView2.imageTintList = if (station.state == StationState.UNDERGROUND) ColorStateList.valueOf(Color.RED) else ColorStateList.valueOf(Color.BLUE)
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

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Station>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SearchViewVh, position: Int) {

        holder.onBind(list[position])
        holder.itemView.setOnClickListener {
            onItemClickListener(list[position])
        }
    }

    private fun getLineColor(line: Line): String {
        return when (line) {
            Line.CHILANZAR -> "#FF453A"
            Line.UZBEKISTAN -> "#0B84FF"
            Line.YUNUSOBOD -> "#31D158"
            Line.INDEPENDENCEDAY -> "#FED709"
        }
    }
    private fun drawGradient(centerColor: String): GradientDrawable {
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                Color.parseColor("#E6F1FF"),
                Color.parseColor(centerColor),
                Color.parseColor("#E6F1FF")
            )
        )
        gradientDrawable.cornerRadius = 5f
        return gradientDrawable
    }
}