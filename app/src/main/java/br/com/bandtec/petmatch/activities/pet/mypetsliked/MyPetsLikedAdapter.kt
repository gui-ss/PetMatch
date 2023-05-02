package br.com.bandtec.petmatch.activities.pet.mypetsliked

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.data.model.Match
import kotlinx.android.synthetic.main.layout_card_my_pets_liked.view.*

class MyPetsLikedAdapter(
    private val pets: List<Match>?,
    private val context: Context,
    private val onItemClickListener: (idPet: Int) -> Unit
): RecyclerView.Adapter<MyPetsLikedAdapter.MyPetsLikedViewHolder>() {

    private var getIdAdotante = 0

    class MyPetsLikedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titleMatch = itemView.tv_title
        val descriptionMatch = itemView.tv_description_like

        val imagePet = itemView.img_preview2
        val imageAdotante = itemView.img_preview

        fun onClick(idPet: Int, execute: (idPet: Int) -> Unit){
            itemView.setOnClickListener {
                execute(idPet)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPetsLikedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_my_pets_liked, parent, false)
        return MyPetsLikedViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyPetsLikedViewHolder, position: Int) {
        val pets = pets?.get(position)
        holder.let {
            it.titleMatch.text = "${pets?.nomeAdotante} gostou do(a) ${pets?.nomePet}."
            it.descriptionMatch.text = "Veja as informaçôes de ${pets?.nomeAdotante} e curta para que o pet possa ser adotado."

           //Picasso.get().load(pets?.fotoPet).resize(1550, 1000).into(it.imagePet)
           //Picasso.get().load(pets?.fotoAdotante).resize(1550, 1000).into(it.imageAdotante)

            val imageBytesPet = Base64.decode(pets?.fotoPet, Base64.DEFAULT)
            val decodedImagePet = BitmapFactory.decodeByteArray(imageBytesPet, 0, imageBytesPet.size)
            it.imagePet.setImageBitmap(decodedImagePet)

            val imageBytes = Base64.decode(pets?.fotoAdotante, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            it.imageAdotante.setImageBitmap(decodedImage)

            getIdAdotante = pets!!.idAdotante
            val preferences: SharedPreferences = context.getSharedPreferences("id_adotante", Context.MODE_PRIVATE)
            setIdUser(preferences)



        }

        holder.let{
            pets?.idPet?.let { it1 -> it.onClick(it1, onItemClickListener) }
        }
    }

     fun setIdUser(preferences: SharedPreferences){
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putInt("id_adotante", getIdAdotante)
        editor.apply()
    }

    override fun getItemCount(): Int {
        return pets?.size ?: 0
    }
}