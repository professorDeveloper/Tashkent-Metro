package com.azamovhudstc.tashkentmetro.ui.screens.map

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.tashkentmetro.data.model.station.Station
import com.azamovhudstc.tashkentmetro.data.model.station.StationState
import com.azamovhudstc.tashkentmetro.databinding.ItemPopularStationsBinding
import com.azamovhudstc.tashkentmetro.utils.LocalData

class PopularStationAdapter : RecyclerView.Adapter<PopularStationAdapter.SVHolder>() {
    private var list: MutableList<Station> = LocalData.popularStations.toMutableList()

    inner class SVHolder(private val itemBinding: ItemPopularStationsBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun onBind(model: Station, previousLineName: String?) {
            itemBinding.popularStation.text = model.name
            itemBinding.stationPosition.text = model.state.name

            if (previousLineName != model.line.name) {
                itemBinding.linearLayout5.visibility = View.VISIBLE
                itemBinding.stationLine.text = model.line.name
            } else {
                itemBinding.linearLayout5.visibility = View.GONE
            }


            val icon = itemBinding.imageView2
            icon.imageTintList = if (model.state == StationState.UNDERGROUND) ColorStateList.valueOf(Color.RED) else ColorStateList.valueOf(Color.BLUE)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SVHolder {
        return SVHolder(
            ItemPopularStationsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SVHolder, position: Int) {
        val previousLineName = if (position > 0) list[position - 1].line.name else null
        holder.onBind(list[position],previousLineName)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Station>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun getStationAt(position: Int): Station {
        return list[position]
    }

}