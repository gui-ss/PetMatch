package br.com.bandtec.petmatch.activities.pet.mypets

import android.R.attr.bitmap
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.data.model.Pet
import kotlinx.android.synthetic.main.item_view_pager.view.*
import kotlinx.android.synthetic.main.layout_card_my_pets.view.*


class MyPetsAdapter(
    private val petsName: List<Pet>?,
    private val context: Context,
    private val onItemClickListener: (idPet : Int) -> Unit
): RecyclerView.Adapter<MyPetsAdapter.MyPetsViewHolder>() {

    class MyPetsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name = itemView.tv_name_pet
        val imagePet = itemView.img_pet

        fun onClick(idPet: Int, execute: (idPet: Int) -> Unit){
            itemView.setOnClickListener {
                execute(idPet)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPetsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_my_pets, parent, false)
        return MyPetsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyPetsViewHolder, position: Int) {
       val pets = petsName?.get(position)
        holder.let {
            it.name.text = pets?.petNome
            //Picasso.get().load(pets?.petFotoPerfil).resize(1550, 1000).into(it.imagePet)
            pets?.petFotoPerfil.let {

            }
            val imageBytes = Base64.decode(pets?.petFotoPerfil, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            val d: Drawable = BitmapDrawable(Bitmap.createBitmap(decodedImage))
            it.imagePet.setBackgroundDrawable(d)
        }

        holder?.let {
            pets?.id?.let { it1 -> it.onClick(it1.toInt(), onItemClickListener) }
        }

    }

    override fun getItemCount(): Int {
        return petsName?.size ?: 0
    }
}