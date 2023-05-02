package br.com.bandtec.petmatch.activities.pet.aboutpet

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.pet.carouselcardpets.CarouselCardPetActivity
import br.com.bandtec.petmatch.activities.pet.editpet.EditPetActivity
import br.com.bandtec.petmatch.activities.pet.mypets.MyPetsAdapter
import br.com.bandtec.petmatch.data.Endpoint
import br.com.bandtec.petmatch.data.NetworkUtils
import br.com.bandtec.petmatch.data.model.Like
import br.com.bandtec.petmatch.data.model.Pet
import br.com.bandtec.petmatch.data.model.Vacina
import kotlinx.android.synthetic.main.activity_about_pet.*
import kotlinx.android.synthetic.main.activity_my_pets.*
import kotlinx.android.synthetic.main.item_view_pager.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AboutPetActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    private lateinit var btnFavorite: Button

    private var checkDataPet = 0

    private var idPet = 0

    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_pet)

        btnBack = findViewById(R.id.arrowBack)
        btnFavorite = findViewById(R.id.btnFav)

        btnBack.setOnClickListener {
            val intent = Intent(this, CarouselCardPetActivity::class.java)
            startActivity(intent)
            finish()
        }

        val idSelected = intent.getIntExtra("ID_PET_SELECTED", 0)
        findPet(idSelected.toString())


        btnFavorite.setOnClickListener {
            if(!btnFavorite.background.setTint(ContextCompat.getColor(baseContext, R.color.red)).equals(
                    ContextCompat.getColor(baseContext, R.color.red)
            )){
                btnFavorite.background.setTint(ContextCompat.getColor(baseContext, R.color.red))
                getData(
                    Like(
                        idSelected,
                        true
                )
                )
            }else{
                btnFavorite.background.setTint(ContextCompat.getColor(baseContext, R.color.grey_2))
            }
        }
    }

    private fun configureList(vaccines: List<Vacina>) {
        val recyclerView = vaccines_recyclerview
        recyclerView.adapter = VaccineAdapter(vaccines, this)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
    }

    private fun getData(dataLike: Like){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .darLike(dataLike)
            .enqueue(object : Callback<Like> {
                override fun onResponse(call: Call<Like>, response: Response<Like>) {
                    response.code()
                    Log.d("like", "sucesso")
                }
                override fun onFailure(call: Call<Like>, t: Throwable) {
                    Log.d("like", "erro")
                }

            })
    }

    fun findPet(id: String){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .getPetById(id = id)
            .enqueue(object : Callback<Pet> {
                override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                    if (response.isSuccessful)

                        checkDataPet = 1
                        checkLoading()
                        response.body()
                        tv_name_pet.text = response.body()?.petNome

                    val imageBytes = Base64.decode(response.body()?.petFotoPerfil, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    val d: Drawable = BitmapDrawable(Bitmap.createBitmap(decodedImage))
                    img_pet.setBackgroundDrawable(d)

                    response.body()?.petSexo = if (response.body()?.petSexo == "M"){
                        "Macho"
                    }else{
                        "Fêmea"
                    }

                    response.body()?.petPorte = if (response.body()?.petPorte == "P"){
                        "Pequeno"
                    }else if(response.body()?.petPorte == "M"){
                        "Médio"
                    }else{
                        "Grande"
                    }

                    response.body()?.petCastrado = if (response.body()?.petCastrado == "true"){
                        "Sim"
                    }else{
                        "Não"
                    }

                    response.body()?.petSociavel = if (response.body()?.petSociavel == "true"){
                        "Sim"
                    }else{
                        "Não"
                    }

                        tvInfoPet.text = "Idade: ${response.body()?.petIdade} anos " +
                                "\nSexo: ${response.body()?.petSexo}" +
                                "\nPorte: ${response.body()?.petPorte}" +
                                "\nCastrado: ${response.body()?.petCastrado}" +
                                "\nSociável: ${response.body()?.petSociavel}" +
                                "\nComportamento: ${response.body()?.petComportamento}"

                    idPet = response.body()?.id!!.toInt()
                    findVaccines(idPet)
                }

                override fun onFailure(call: Call<Pet>, t: Throwable) {
                    Toast.makeText(baseContext, "Falha ao curtir.", Toast.LENGTH_SHORT).show()
                    checkDataPet = 2
                    checkLoading()
                }

            })
    }

    fun findVaccines(id: Int){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .listarVacinas(id = id)
            .enqueue(object : Callback <List<Vacina>> {
                override fun onResponse(call: Call<List<Vacina>>, response: Response<List<Vacina>>) {
                    response.body()?.let {
                        val vaccines: List<Vacina> = it
                        configureList(vaccines)
                    }
                }
                override fun onFailure(call: Call<List<Vacina>>, t: Throwable) {
                    Toast.makeText(baseContext, "Falha ao carregar vacinas.", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun checkLoading(){
        if (checkDataPet == 1){
            clAboutPet.visibility = View.VISIBLE
            animationView.visibility = View.GONE
        }else{
            animationViewError.visibility = View.VISIBLE
            animationView.visibility = View.GONE
        }
    }
}