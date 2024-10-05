
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.tashkentmetro.data.model.station.EndStation
import com.azamovhudstc.tashkentmetro.data.model.station.Line
import com.azamovhudstc.tashkentmetro.data.model.station.MiddleStation
import com.azamovhudstc.tashkentmetro.data.model.station.StartStation
import com.azamovhudstc.tashkentmetro.data.model.station.StationItem
import com.azamovhudstc.tashkentmetro.databinding.ItemLineEndBinding
import com.azamovhudstc.tashkentmetro.databinding.ItemLineMiddleBinding
import com.azamovhudstc.tashkentmetro.databinding.ItemTimelineStationBinding
import com.azamovhudstc.tashkentmetro.utils.gone
import com.azamovhudstc.tashkentmetro.utils.visible
import com.lriccardo.timelineview.TimelineAdapter
import com.lriccardo.timelineview.TimelineView

class TimelineAdapter(private val items: List<StationItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), TimelineAdapter {

    companion object {
        private const val VIEW_TYPE_START = 0
        private const val VIEW_TYPE_END = 1
        private const val VIEW_TYPE_MIDDLE = 2
    }

    inner class StartViewHolder(val binding: ItemTimelineStationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(station: StartStation) {
            binding.stationName.text = station.name
            binding.lineName.text = station.line
            binding.stationTime.text = station.time
            binding.cardCircle.setCardBackgroundColor( Color.parseColor(getLineColor(station.line)))
            setLineColor(station.line,binding.timelineView)

        }
    }

    inner class EndViewHolder(val binding: ItemLineEndBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(station: EndStation) {
            binding.stationName.text = station.name
            binding.stationTime.text = station.time
            if (items[items.size - 1] == station){
                binding.transferTv.gone()
            }else{
                binding.transferTv.visible()
            }
            setLineColor(station.line,binding.timelineView)
        }
    }

    inner class MiddleViewHolder(val binding: ItemLineMiddleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(station: MiddleStation) {
            binding.stationName.text = station.name
            setLineColor(station.line,binding.timelineView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_START -> {
                val binding = ItemTimelineStationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                StartViewHolder(binding)
            }
            VIEW_TYPE_END -> {
                val binding = ItemLineEndBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EndViewHolder(binding)
            }
            VIEW_TYPE_MIDDLE -> {
                val binding = ItemLineMiddleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MiddleViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is StartStation -> (holder as StartViewHolder).bind(item)
            is EndStation -> (holder as EndViewHolder).bind(item)
            is MiddleStation -> (holder as MiddleViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is StartStation -> VIEW_TYPE_START
            is EndStation -> VIEW_TYPE_END
            is MiddleStation -> VIEW_TYPE_MIDDLE
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    override fun getTimelineViewType(position: Int): TimelineView.ViewType? {
        return when (position) {
            0 -> TimelineView.ViewType.FIRST
            items.size - 1 -> TimelineView.ViewType.LAST
            else -> TimelineView.ViewType.MIDDLE
        }
    }

    override fun getIndicatorDrawable(position: Int): Drawable? {
        return null
    }

    @DrawableRes
    override fun getIndicatorDrawableRes(position: Int): Int? {
        return null
    }

    override fun getIndicatorStyle(position: Int): TimelineView.IndicatorStyle? {
        return TimelineView.IndicatorStyle.Filled
    }

    override fun getIndicatorColor(position: Int): Int? {
        return Color.parseColor("#EC5656")
    }

    override fun getLineColor(position: Int): Int? {
        return Color.parseColor("#EC5656")
    }

    override fun getLineStyle(position: Int): TimelineView.LineStyle? {
        return when (position) {
            0 -> TimelineView.LineStyle.Normal
            items.size - 1 -> TimelineView.LineStyle.Dashed
            else -> TimelineView.LineStyle.Normal
        }
    }

    override fun getLinePadding(position: Int): Float? {
        return 10f
    }

    private fun setLineColor(station: String, timelineView: TimelineView){
        val color = getLineColor(station)
        timelineView.indicatorColor = Color.parseColor(color)
        timelineView.lineColor = Color.parseColor(color)
    }
    private fun getLineColor(line: String): String {
        return when (line) {
            Line.CHILANZAR.name -> "#FF453A"
            Line.UZBEKISTAN.name -> "#0B84FF"
            Line.YUNUSOBOD.name -> "#31D158"
            Line.INDEPENDENCEDAY.name -> "#FED709"
            else -> {"#FF453A"}
        }
    }
}