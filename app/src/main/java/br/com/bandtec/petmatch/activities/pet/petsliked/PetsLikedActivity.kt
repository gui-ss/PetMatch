package br.com.bandtec.petmatch.activities.pet.petsliked

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity
import br.com.bandtec.petmatch.data.Endpoint
import br.com.bandtec.petmatch.data.NetworkUtils
import br.com.bandtec.petmatch.data.model.Match
import kotlinx.android.synthetic.main.activity_pets_liked.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetsLikedActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    private var checkDataPet = 0

    private var checkList: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets_liked)

        btnBack = findViewById<ImageView>(R.id.arrowBack)

        btnBack.setOnClickListener {
            onBackScreen()
        }

        getData()
    }

    private fun configureList(pets: List<Match>) {
        val recyclerView = pets_liked_recyclerview
        recyclerView.adapter = PetsLikedAdapter(pets, this)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
    }

    private fun onBackScreen(){
        val goHomeScreen = Intent(this, HomeActivity::class.java)
        startActivity(goHomeScreen)
    }

    private fun getData(){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .listPetsLiked()
            .enqueue(object : Callback<List<Match>> {
                override fun onResponse(call: Call<List<Match>>, response: Response<List<Match>>) {
                    response.body()?.let {
                        val pets: List<Match> = it
                        configureList(pets)
                        checkDataPet = 1
                        checkList = pets.size
                        checkLoading()
                    }
                }

                override fun onFailure(call: Call<List<Match>>, t: Throwable) {
                    Toast.makeText(baseContext, "Não foi possível carregar os pets.", Toast.LENGTH_SHORT).show()
                    checkDataPet = 2

                    checkLoading()
                }

            })
    }

    private fun checkLoading(){
        if (checkDataPet == 1){
            if (checkList!! >= 1){
                scrollViewPetsLiked.visibility = View.VISIBLE
                animationViewPetsLiked.visibility = View.GONE
            }else{
                animationViewErrorPetsLiked.visibility = View.VISIBLE
                scrollViewPetsLiked.visibility = View.VISIBLE
                animationViewPetsLiked.visibility = View.GONE
            }
        }else if(checkDataPet == 2){
            animationViewErrorPetsLiked.visibility = View.VISIBLE
            animationViewPetsLiked.visibility = View.GONE
        }
    }
}