package br.com.bandtec.petmatch.activities.pet.mypetsliked

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.match.MatchActivity
import br.com.bandtec.petmatch.activities.pet.mypets.MyPetsActivity
import br.com.bandtec.petmatch.data.CepAPI
import br.com.bandtec.petmatch.data.Endpoint
import br.com.bandtec.petmatch.data.NetworkUtils
import br.com.bandtec.petmatch.data.model.Cep
import br.com.bandtec.petmatch.data.model.Match
import br.com.bandtec.petmatch.data.model.User
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.activity_match.*
import kotlinx.android.synthetic.main.dialog_match.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DialogMatch: DialogFragment() {

    private lateinit var btnAcceptMatch: Button
    private lateinit var btnRefuseMatch: Button
    private lateinit var btnClose: Button

    private lateinit var tvName: TextView
    private lateinit var tvLocal: TextView
    private lateinit var tvDescription: TextView
    private lateinit var imgUser: ImageView

    private lateinit var cardMatch: ConstraintLayout
    private lateinit var loadingMatch: LottieAnimationView

    private var checkDataPet = 0

    private var matchAccept: Boolean = false
    private var idPet: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idPet = arguments?.getInt("idPet")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val _view: View? = activity?.layoutInflater?.inflate(R.layout.dialog_match, null)

        this.tvName = _view!!.findViewById(R.id.tv_name_adotante)
        this.tvLocal = _view.findViewById(R.id.tv_local_adotante)
        this.tvDescription = _view.findViewById(R.id.tv_description_adotante)
        this.imgUser = _view.findViewById(R.id.img_adotanter)

        this.cardMatch = _view.findViewById(R.id.cardMatch)
        this.loadingMatch = _view.findViewById(R.id.loadingMatch)

        this.btnAcceptMatch = _view.findViewById(R.id.btnAcceptMatch)
        this.btnRefuseMatch = _view.findViewById(R.id.btnRefuseMatch)
        this.btnClose = _view.findViewById(R.id.btnClose)

        val preferencesName: SharedPreferences =
            requireContext().getSharedPreferences("id_adotante", Context.MODE_PRIVATE)
        val idAdotante = preferencesName.getInt("id_adotante", 0)

        getUser(idAdotante.toString())

        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setView(_view)


        this.btnAcceptMatch.setOnClickListener {
            match(idAdotante, idPet!!)

            cardMatch.visibility = View.GONE
            loadingMatch.visibility = View.VISIBLE
        }

        this.btnRefuseMatch.setOnClickListener {
            recusarMatch(idAdotante, idPet!!)

            cardMatch.visibility = View.GONE
            loadingMatch.visibility = View.VISIBLE
        }

        this.btnClose.setOnClickListener {
           dismiss()
        }

        return alertDialog.create()
    }

    fun match(idAdotante: Int, idPet: Int){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .darMatch(idAdotante, idPet)
            .enqueue(object : Callback<Match> {
                override fun onResponse(call: Call<Match>, response: Response<Match>) {
                    matchAccept = true

                    val editor = context?.getSharedPreferences("id_adotante", 0)?.edit()
                    editor!!.clear()
                    editor.apply()
                    dismiss()
                    val intent = Intent(context, MatchActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<Match>, t: Throwable) {
                    loadingMatch.visibility = View.GONE
                    dismiss()
                    val editor = context?.getSharedPreferences("id_adotante", 0)?.edit()
                    editor!!.clear()
                    editor.apply()
                    val intent = Intent(context, MatchActivity::class.java)
                    startActivity(intent)
                }

            })
    }

    fun recusarMatch(idAdotante: Int, idPet: Int){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .recusarMatch(idAdotante, idPet)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    matchAccept = true
                    loadingMatch.visibility = View.GONE
                    val editor = context?.getSharedPreferences("id_adotante", 0)?.edit()
                    editor!!.clear()
                    editor.apply()
                    dismiss()
                    val intent = Intent(context, MyPetsLikedActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    loadingMatch.visibility = View.GONE
                    dismiss()
                    val editor = context?.getSharedPreferences("id_adotante", 0)?.edit()
                    editor!!.clear()
                    editor.apply()
                    val intent = Intent(context, MyPetsLikedActivity::class.java)
                    startActivity(intent)
                }

            })
    }

    fun checkMatchAccept(): Boolean{
        return matchAccept
    }

    fun getUser(id: String){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .getUser(id = id)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    tvName.text = response.body()?.usuario_nome
                    tvDescription.text = "${response.body()?.usuario_nome} curtiu seu pet, caso\ndeseje continuar para uma\npossível adoção, basta aprovar ou\ncaso não queria, basta recusar."
                    val imageBytes = Base64.decode(response.body()?.usuario_foto_perfil, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    imgUser.setImageBitmap(decodedImage)
                    findCepBy(response.body()?.usuario_cep!!)
                    checkDataPet = 1
                    checkLoading()
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(context, "Dados indisponiveis, por favor, tente novamente.", Toast.LENGTH_SHORT).show()
                }

            })
    }


    fun findCepBy(cep: String){
        NetworkUtils.retrofitCep().create(CepAPI::class.java)
            .findCep(cep = cep)
            .enqueue(object : Callback<Cep> {
                override fun onFailure(call: Call<Cep>, t: Throwable) {
                    Toast.makeText(context, "Erro", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Cep>, response: Response<Cep>) {
                    tvLocal.text = "${response.body()?.city} - ${response.body()?.state}"
                }

            })
    }

    private fun checkLoading(){
        if (checkDataPet == 1){
            cardMatch.visibility = View.VISIBLE
            loadingMatch.visibility = View.GONE
        }else{
            loadingMatch.visibility = View.VISIBLE
        }
    }
}