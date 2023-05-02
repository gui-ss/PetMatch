package br.com.bandtec.petmatch.activities.pet.mypets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity
import br.com.bandtec.petmatch.activities.pet.editpet.EditPetActivity
import br.com.bandtec.petmatch.data.Endpoint
import br.com.bandtec.petmatch.data.NetworkUtils
import br.com.bandtec.petmatch.data.model.Match
import br.com.bandtec.petmatch.data.model.Pet
import kotlinx.android.synthetic.main.activity_carousel_card_pet.*
import kotlinx.android.synthetic.main.activity_my_pets.*
import kotlinx.android.synthetic.main.activity_my_pets.scrollView
import kotlinx.android.synthetic.main.activity_my_pets_liked.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPetsActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    private var checkDataPet = 0

    private var checkList: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pets)

        btnBack = findViewById<ImageView>(R.id.arrowBack)

        btnBack.setOnClickListener {
            onBackScreen()
        }

        getData()

    }


    private fun configureList(pets: List<Pet>) {
        val recyclerView = mypets_list_recyclerview

        recyclerView.adapter = MyPetsAdapter(pets, this){
            val intent = Intent(this, EditPetActivity::class.java)
            intent.putExtra("ID_PET_SELECTED_EDIT", it)
            startActivity(intent)
        }
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
    }

    private fun onBackScreen(){
        val goHomeScreen = Intent(this, HomeActivity::class.java)
        startActivity(goHomeScreen)
    }

    private fun getData(){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .listMyPets()
            .enqueue(object : Callback<List<Pet>>{
                override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                    if (response.code() == 404){
                        checkDataPet = 2
                        checkList = 0
                        checkLoading()
                    }else{
                        response.body()?.let {
                            val pets: List<Pet> = it
                            configureList(pets)
                            checkDataPet = 1
                            checkList = pets.size
                            checkLoading()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                    Toast.makeText(baseContext, "Não foi possível carregar os pets.", Toast.LENGTH_SHORT).show()
                    checkDataPet = 2

                    checkLoading()
                }

            })
    }

    private fun checkLoading(){
        if (checkDataPet == 1){
            if (checkList!! >= 1){
                scrollView.visibility = View.VISIBLE
                animationViewMyPets.visibility = View.GONE
            }else{
                animationViewErrorMyPets.visibility = View.VISIBLE
                scrollView.visibility = View.VISIBLE
                animationViewMyPets.visibility = View.GONE
            }
        }else if(checkDataPet == 2){
            animationViewErrorMyPets.visibility = View.VISIBLE
            animationViewMyPets.visibility = View.GONE
        }
    }
}