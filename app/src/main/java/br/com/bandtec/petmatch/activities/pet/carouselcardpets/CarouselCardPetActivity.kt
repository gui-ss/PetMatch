package br.com.bandtec.petmatch.activities.pet.carouselcardpets

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity
import br.com.bandtec.petmatch.activities.pet.aboutpet.AboutPetActivity
import br.com.bandtec.petmatch.data.FilterRemoteDataSource
import br.com.bandtec.petmatch.data.model.Filter
import br.com.bandtec.petmatch.data.model.Pet
import br.com.bandtec.petmatch.presentation.FilterPresenter
import kotlinx.android.synthetic.main.activity_carousel_card_pet.*
import kotlinx.android.synthetic.main.activity_carousel_card_pet.arrowBack
import kotlinx.android.synthetic.main.activity_my_pets.*


class CarouselCardPetActivity : AppCompatActivity() {

    private lateinit var showPet: TextView

    private var listPetsName: List<String>? = null
    private var listPetsImage: List<String>? = null
    private var listPetsPetWeek: List<Int>? = null
    private var listPetsId: List<Int>? = null
    private var listId: List<String>? = null

    private var checkDataPet = 0

    private var checkList: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carousel_card_pet)

        arrowBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        showPet = findViewById(R.id.titleToolbar)

        showPet.setOnClickListener {

        }

        val dataSource: FilterRemoteDataSource = FilterRemoteDataSource()
        val presenter: FilterPresenter = FilterPresenter(this, dataSource)
        presenter.filterPet(getDataFilter())

    }

    private fun getDataFilter(): Filter {
        var nickname = intent.getStringExtra("NICKNAME_FILTER")
        var agePet = intent.getStringExtra("AGE_FILTER")
        var breed = intent.getStringExtra("BREED_FILTER")
        var behavior = intent.getStringExtra("BEHAVIOR_FILTER")
        var size = intent.getStringExtra("SIZE_FILTER")
        var sex = intent.getStringExtra("SEX_FILTER")
        var sociable = intent.getStringExtra("SOCIABLE_FILTER")
        var castrated = intent.getStringExtra("CASTRATED_FILTER")
        var specie = intent.getStringExtra("SPECIE_FILTER")
        var parametros = "true"


        if (nickname == "null" && agePet == "null" && breed == "null" && behavior == "null" && size == "null" &&
                sex == "null" && sociable == "null" && castrated == "null" && specie == "null"){
            parametros = "false"
        }

        if (nickname == ""){
            nickname = null
        }

        if (agePet == ""){
            agePet = null
        }

        if (breed == ""){
            breed = null
        }

        if (behavior == ""){
            behavior = null
        }

        if (size == ""){
            size = null
        }

        if (sex == ""){
            sex = null
        }

        if (sociable == ""){
            sociable = null
        }

        if (castrated == ""){
            castrated = null
        }

        if (specie == ""){
            specie = null
        }


        return Filter(
            agePet,
            nickname,
            specie ,
            breed,
            size,
            sex,
            castrated,
            sociable,
            behavior,
            parametros
        )
    }

    fun getPets(response: List<Pet>?){
        if (response?.size!! == 1){
            tv_description_filter.text = "Encontramos ${response?.size.toString()} pet para você com as características desejadas."
            checkDataPet = 1
            checkList = response.size
            checkLoading()
        }else if(response.size >= 2){
            tv_description_filter.text = "Encontramos ${response?.size.toString()} pets para você com as características desejadas."
            checkDataPet = 1
            checkList = response.size
            checkLoading()
        }else{
            checkDataPet = 2
            checkList = 0
            checkLoading()
        }

        listPetsName = response?.map {
            it.petNome
        }

        listPetsImage = response?.map{
                        it.petFotoPerfil
        }

        listPetsPetWeek = response?.map{
                        if (it.isPetWeek == "true"){
                            R.drawable.ic_petweek
                        }else{
                            R.drawable.ic_petweek_disable
                        }
                    }

        listPetsId = response?.map {
            it.id.toInt()
        }


        val adapter = ViewPagerAdapter(listPetsName!!, listPetsImage!!, listPetsPetWeek!!, listPetsId){

            val intent = Intent(this, AboutPetActivity::class.java)
            intent.putExtra("ID_PET_SELECTED", it)
            startActivity(intent)

        }
        viewPager.adapter = adapter
    }

    private fun checkLoading(){
        if (checkDataPet == 1){
            if (checkList!! >= 1){
                clGridPets.visibility = View.VISIBLE
                animationViewCarousel.visibility = View.GONE
            }else{
                animationViewErrorCarousel.visibility = View.VISIBLE
                scrollView.visibility = View.VISIBLE
                animationViewCarousel.visibility = View.GONE
            }
        }else if(checkDataPet == 2){
            animationViewErrorCarousel.visibility = View.VISIBLE
            animationViewCarousel.visibility = View.GONE
        }
    }

}