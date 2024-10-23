package com.zbekz.tashkentmetro.ui.screens.map

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Zbekz.tashkentmetro.databinding.ItemPopularStationsBinding
import com.zbekz.tashkentmetro.data.model.station.Line
import com.zbekz.tashkentmetro.data.model.station.Station
import com.zbekz.tashkentmetro.data.model.station.StationState
import com.zbekz.tashkentmetro.data.model.station.localizeStationState
import com.zbekz.tashkentmetro.utils.LocalData
import com.zbekz.tashkentmetro.utils.currContext
import com.zbekz.tashkentmetro.utils.formatString

class PopularStationAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<PopularStationAdapter.SVHolder>() {
    private var list: MutableList<Station> = LocalData.popularStations.toMutableList()
    var isPopular = true
    var searchStation: Station? = null
    interface OnItemClickListener {
        fun onItemClick(station: Station)
    }
    inner class SVHolder(private val itemBinding: ItemPopularStationsBinding) : RecyclerView.ViewHolder(itemBinding.root){
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(list[position])
                }
            }
        }
        fun onBind(model: Station, previousLineName: String?, position: Int) {
            if (model == searchStation){
                itemView.isEnabled = false
                itemView.alpha = 0.3f
            }else{
                itemView.isEnabled = true
                itemView.alpha = 1f

            }
            itemBinding.popularStation.text = formatString(model.name, context = currContext()!!)
            itemBinding.stationPosition.text = localizeStationState(model.state, itemBinding.root.context)


            if (previousLineName != model.line.name && !isPopular && position > 0) {
                itemBinding.linearLayout5.visibility = View.VISIBLE
                itemBinding.stationLine.text = model.line.name
                itemBinding.gradientView.background = drawGradient(getLineColor(model.line))
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
        holder.onBind(list[position],previousLineName, position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Station>, i: Boolean, station: Station? = null) {
        isPopular = i
        list.clear()
        searchStation = station
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun getStationAt(position: Int): Pair<Station,GradientDrawable> {
        val model = list[position]
        val gradient = drawGradient(getLineColor(model.line))
        return Pair(model,gradient)
    }

    private fun getLineColor(line: Line): String {
        return when (line) {
            Line.CHILANZAR -> "#FF453A"
            Line.UZBEKISTAN -> "#0B84FF"
            Line.YUNUSABAD -> "#31D158"
            Line.INDEPENDENCEDAY -> "#FED709"
        }
    }
    private fun drawGradient(centerColor: String): GradientDrawable {
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                Color.TRANSPARENT,
                Color.parseColor(centerColor),
                Color.TRANSPARENT
            )
        )
        gradientDrawable.cornerRadius = 5f
        return gradientDrawable
    }

}