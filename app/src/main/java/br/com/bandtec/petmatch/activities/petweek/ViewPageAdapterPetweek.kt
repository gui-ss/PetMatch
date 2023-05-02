package br.com.bandtec.petmatch.activities.petweek

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.bandtec.petmatch.R
import kotlinx.android.synthetic.main.item_view_pager.view.*
import kotlinx.android.synthetic.main.layout_card_petweek.view.*

class ViewPageAdapterPetweek (
    val titles: List<String>, val images: List<Int>, val texts: List<String>, val paws: List<Int>
    ): RecyclerView.Adapter<ViewPageAdapterPetweek.ViewPagerViewHolder>(){

    inner class ViewPagerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_petweek, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {

        val curTitle = titles[position]
        holder.itemView.titleToolbar.text = curTitle.toString()

        val curImage = images[position]
        holder.itemView.img_petweekViewPage.setImageResource(curImage)

        val curText = texts[position]
        holder.itemView.tv_text.text = curText.toString()

        val curPaw = paws[position]
        holder.itemView.ic_paws.setImageResource(curPaw)

    }

    override fun getItemCount(): Int {
        return images.size
    }
}