package br.com.bandtec.petmatch.activities.pet.mypetsliked

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity
import br.com.bandtec.petmatch.data.Endpoint
import br.com.bandtec.petmatch.data.NetworkUtils
import br.com.bandtec.petmatch.data.model.Match
import kotlinx.android.synthetic.main.activity_my_pets_liked.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPetsLikedActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    private var checkDataPet = 0

    private var checkList = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pets_liked)

        btnBack = findViewById<ImageView>(R.id.arrowBack)

        btnBack.setOnClickListener {
            onBackScreen()
        }

        getData()
    }

    private fun configureList(pets: List<Match>) {
        val recyclerView = mypetsliked_list_recyclerview
        recyclerView.adapter = MyPetsLikedAdapter(pets, this){
            val dialog: DialogMatch = DialogMatch()
            dialog.show(supportFragmentManager,"dialog")

            val bundle = bundleOf( "idPet" to it)
            dialog.arguments = bundle
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
            .listMyPetsLiked()
            .enqueue(object : Callback<List<Match>> {
                override fun onResponse(call: Call<List<Match>>, response: Response<List<Match>>) {
                    if (response.code() == 404){
                        checkDataPet = 2
                        checkList = 0
                        checkLoading()
                    }else{
                        response.body()?.let {
                            val pets: List<Match> = it
                            configureList(pets)
                            checkDataPet = 1
                            checkList = pets.size
                            checkLoading()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Match>>, t: Throwable) {
                    checkDataPet = 2
                    checkList = 0
                    checkLoading()
                }

            })
    }

    private fun checkLoading(){
        if (checkDataPet == 1){
            if (checkList == 0){
                animationViewMyPetsLiked.visibility = View.GONE
                animationViewErrorMyPetsLiked.visibility = View.VISIBLE
            }else{
                animationViewMyPetsLiked.visibility = View.GONE
                scrollViewMyPetsLiked.visibility = View.VISIBLE
            }
        }else if(checkDataPet == 2){
            animationViewMyPetsLiked.visibility = View.GONE
            animationViewErrorMyPetsLiked.visibility = View.VISIBLE
        }
    }

}