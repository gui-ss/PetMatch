package br.com.bandtec.petmatch.activities.match

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.data.model.Match
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_card_match.view.*

class MatchAdapter(
private val pets: List<Match>?,
private val context: Context
): RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    class MatchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titleMatch = itemView.tv_description
        val descriptionDoador = itemView.tv_description_info_doador
        val descriptionAdotante = itemView.tv_description_info_adotante

        val imagePet = itemView.img_pet
        val imageAdotante = itemView.img_adotante
        val imageDoador = itemView.img_doador
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val pets = pets?.get(position)
        holder.let {
            it.titleMatch.text = "${pets?.nomeDoador} aprovou ${pets?.nomeAdotante} para uma possível adoção de ${pets?.nomePet}."

            it.descriptionDoador.text = "${pets?.nomeDoador}\n São Paulo - SP\nEmail: ${pets?.emailDoador}\nTelefone: ${pets?.telefoneDoador}"
            it.descriptionAdotante.text = " ${pets?.nomeAdotante}\n São Paulo - SP\nEmail: ${pets?.emailAdotante}\nTelefone: ${pets?.telefoneAdotante}"

            val imageBytesPet = Base64.decode(pets?.fotoPet, Base64.DEFAULT)
            val decodedImagePet = BitmapFactory.decodeByteArray(imageBytesPet, 0, imageBytesPet.size)
            it.imagePet.setImageBitmap(decodedImagePet)

            val imageBytes = Base64.decode(pets?.fotoAdotante, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            it.imageAdotante.setImageBitmap(decodedImage)

            val imageBytes3 = Base64.decode(pets?.fotoDoador, Base64.DEFAULT)
            val decodedImage3 = BitmapFactory.decodeByteArray(imageBytes3, 0, imageBytes3.size)
            it.imageDoador.setImageBitmap(decodedImage3)
        }
    }

    override fun getItemCount(): Int {
        return pets?.size ?: 0
    }
}