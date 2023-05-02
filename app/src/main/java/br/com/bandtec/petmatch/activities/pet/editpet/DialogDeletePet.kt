package br.com.bandtec.petmatch.activities.pet.editpet

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.pet.mypets.MyPetsActivity
import br.com.bandtec.petmatch.data.Endpoint
import br.com.bandtec.petmatch.data.NetworkUtils
import br.com.bandtec.petmatch.data.model.Match
import br.com.bandtec.petmatch.data.model.Pet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogDeletePet: DialogFragment() {

    private lateinit var btnAccept: Button
    private lateinit var btnRefuse: Button
    private lateinit var btnClose: Button

    private var idPet: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idPet = arguments?.getString("idPetDelete")

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val _view: View = requireActivity().layoutInflater.inflate(R.layout.dialog_delete_pet, null)

        this.btnAccept = _view.findViewById(R.id.btnAccept)
        this.btnRefuse = _view.findViewById(R.id.btnRefuse)
        this.btnClose = _view.findViewById(R.id.btnClose)


        var alertDialog = AlertDialog.Builder(activity)
        alertDialog.setView(_view)

        this.btnAccept.setOnClickListener {
            deletePetBy(idPet.toString())
        }

        this.btnRefuse.setOnClickListener {
            dismiss()
        }

        this.btnClose.setOnClickListener {
            dismiss()
        }

        return alertDialog.create()
    }

    private fun deletePetBy(idPet: String){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .deletePetBy(idPet)
            .enqueue(object : Callback<Pet>{
                override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                    Toast.makeText(context, "Pet deletado com sucesso.", Toast.LENGTH_SHORT).show()
                    dismiss()
                    val intent = Intent(context, MyPetsActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<Pet>, t: Throwable) {
                }

            })
    }
}