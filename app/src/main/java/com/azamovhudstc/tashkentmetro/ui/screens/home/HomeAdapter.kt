package com.azamovhudstc.tashkentmetro.ui.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.tashkentmetro.data.model.TrainStatusModel
import com.azamovhudstc.tashkentmetro.databinding.ItemTrainStatusBinding
import com.azamovhudstc.tashkentmetro.utils.slideStart
import com.azamovhudstc.tashkentmetro.utils.slideUp

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private val list = ArrayList<TrainStatusModel>()


    inner class HomeViewHolder(private val itemBinding: ItemTrainStatusBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun onBind(model: TrainStatusModel, pos: Int) {
            itemBinding.containerid.slideUp(700, 0)
            itemBinding.containertop.slideUp(850, 0)
            itemBinding.type1.slideStart(850, 0)
            itemBinding.type2.slideStart(850, 0)
            itemBinding.type3.slideStart(850, 0)
            itemBinding.buttonStatusTrain.slideUp(800, 0)
            itemBinding.buttonCancel.slideUp(800, 0)
            itemBinding.fromTv.text = model.from
            itemBinding.toRv.text = model.to
            itemBinding.buttonStatusTrain.text = model.status

            itemBinding.buttonStatusTrain.setOnClickListener {
                list.remove(model)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, list.size)
            }

            itemBinding.buttonCancel.setOnClickListener {
                list.remove(model)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, list.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemTrainStatusBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }

    fun submitList(newList: ArrayList<TrainStatusModel>) {
        list.clear()
        list.addAll(newList)
    }
}