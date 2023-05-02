package br.com.bandtec.petmatch.activities.pet.aboutpet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.data.model.Pet
import br.com.bandtec.petmatch.data.model.Vacina
import kotlinx.android.synthetic.main.custom_view_vaccines.view.*
import kotlinx.android.synthetic.main.layout_card_my_pets.view.*

class VaccineAdapter (
    private val vaccines: List<Vacina>?,
    private val context: Context,
): RecyclerView.Adapter<VaccineAdapter.VaccineViewHolder>() {

    class VaccineViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val description = itemView.tv_description_vaccine

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_view_vaccines, parent, false)
        return VaccineViewHolder(view)
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        val vaccine = vaccines?.get(position)
        holder.let {
                it.description.text = "${vaccine!!.vacNome} | ${vaccine.vacData} | ${vaccine.vacDose}"
        }

    }

    override fun getItemCount(): Int {
        return vaccines?.size ?: 0
    }
}