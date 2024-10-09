package com.azamovhudstc.tashkentmetro.ui.screens.map

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.data.model.IconPowerMenuItem
import com.skydoves.powermenu.MenuBaseAdapter


class IconMenuAdapter : MenuBaseAdapter<IconPowerMenuItem?>() {
    override fun getView(index: Int, convertView: View?, viewGroup: ViewGroup): View {
        var view = convertView
        val context = viewGroup.context

        // Inflate the view if it's null
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_menu_power, viewGroup, false)
        }

        // Cast the item to IconPowerMenuItem
        val item: IconPowerMenuItem = getItem(index) as IconPowerMenuItem

        // Set the icon resource
        val icon = view!!.findViewById<ImageView>(R.id.itemIcon)
        icon.setImageResource(item.iconRes)

        // Set the title
        val title = view.findViewById<TextView>(R.id.itemTitle)
        if (index > 0) title.setTextColor(context.getColor(R.color.white_and_black)) else title.setTextColor(
            Color.parseColor("#BF463D")
        )
        title.text = item.title

        return view
    }

}