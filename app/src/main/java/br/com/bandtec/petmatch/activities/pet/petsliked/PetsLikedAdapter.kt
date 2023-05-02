package br.com.bandtec.petmatch.activities.pet.petsliked

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.data.model.Match
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_card_pets_liked.view.*

class PetsLikedAdapter(
    private val petsName: List<Match>?,
    private val context: Context
): RecyclerView.Adapter<PetsLikedAdapter.PetsLikedViewHolder>() {

    class PetsLikedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name = itemView.tv_name_pet
        val imagePet = itemView.img_pet

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsLikedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_pets_liked, parent, false)
        return PetsLikedViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetsLikedViewHolder, position: Int) {
        val pets = petsName?.get(position)
        holder.let {
            it.name.text = pets?.nomePet

           // icasso.get().load(pets?.fotoPet).resize(1550, 1000).into(it.imagePet)

            val imageBytesPet = Base64.decode(pets?.fotoPet, Base64.DEFAULT)
            val decodedImagePet = BitmapFactory.decodeByteArray(imageBytesPet, 0, imageBytesPet.size)
            val d: Drawable = BitmapDrawable(Bitmap.createBitmap(decodedImagePet))
            it.imagePet.setBackgroundDrawable(d)
        }

    }

    override fun getItemCount(): Int {
        return petsName?.size ?: 0
    }
}