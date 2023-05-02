package br.com.bandtec.petmatch.activities.match

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity
import br.com.bandtec.petmatch.activities.pet.mypetsliked.DialogMatch
import br.com.bandtec.petmatch.data.Endpoint
import br.com.bandtec.petmatch.data.NetworkUtils
import br.com.bandtec.petmatch.data.model.Match
import kotlinx.android.synthetic.main.activity_match.*
import kotlinx.android.synthetic.main.activity_my_pets_liked.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    private var checkDataPet = 0

    private var checkList: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        btnBack = findViewById<ImageView>(R.id.arrowBack)

        btnBack.setOnClickListener {
            onBackScreen()
        }

        getData()

        checkMatch()
    }

    private fun configureList(pets: List<Match>) {
        val recyclerView = match_list_recyclerview
        recyclerView.adapter = MatchAdapter(pets, this)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
    }

    private fun onBackScreen(){
        val goHomeScreen = Intent(this, HomeActivity::class.java)
        startActivity(goHomeScreen)
    }

    private fun getData(){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .listMatchs()
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
                    Toast.makeText(baseContext, "Não foi possível carregar os matchs.", Toast.LENGTH_SHORT).show()
                    checkDataPet = 2
                    checkList = 0
                    checkLoading()
                }

            })
    }

    private fun checkMatch(){
        val dialogMatch: DialogMatch = DialogMatch()

        if (dialogMatch.checkMatchAccept()){
            finish();
            startActivity(intent);
        }
    }

    private fun checkLoading(){
        if (checkDataPet == 1){
            if (checkList == 0){
                animationViewMatch.visibility = View.GONE
                animationViewErrorMatch.visibility = View.VISIBLE
            }else{
                animationViewMatch.visibility = View.GONE
                scrollViewMatch.visibility = View.VISIBLE
            }
        }else if(checkDataPet == 2){
            animationViewMatch.visibility = View.GONE
            animationViewErrorMatch.visibility = View.VISIBLE
        }
    }

}