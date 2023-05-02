package br.com.bandtec.petmatch.activities.pet.carouselcardpets

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.bandtec.petmatch.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view_pager.view.*

class ViewPagerAdapter(
    private val petsName: List<String>?, private val petsImage: List<String>?,
    private val petsPetWeek: List<Int>?,
    private val petsId: List<Int>?,
    private val onItemClickListener: (idPet : Int) -> Unit):
    RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    class ViewPagerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun onClick(idPet: Int, execute: (idPet: Int) -> Unit){
            itemView.setOnClickListener {
                execute(idPet)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
        return ViewPagerViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {

        val curName = petsName?.get(position)
        holder.itemView.tvNamePet.text = curName

        val curImage = petsImage?.get(position)
        val imageBytes = Base64.decode(curImage, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        val d: Drawable = BitmapDrawable(Bitmap.createBitmap(decodedImage))
        holder.itemView.img_background_pet.setBackgroundDrawable(d)

        val curPetWeek = petsPetWeek?.get(position)
        holder.itemView.ic_petweek.setImageResource(curPetWeek!!)

        val curId = petsId?.get(position)

        holder?.let {
            it.onClick(curId!!, onItemClickListener)
        }

    }


    override fun getItemCount(): Int {
        return petsName?.size ?: 0
    }

}