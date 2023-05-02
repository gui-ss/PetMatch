package br.com.bandtec.petmatch.activities.petweek

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity
import kotlinx.android.synthetic.main.activity_carousel_card_pet.*

class PetWeekActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_week)

        btnBack = findViewById<ImageView>(R.id.arrowBack)

        val titles = listOf(
            getString(R.string.petweek_title1_text),
            getString(R.string.petweek_title2_text),
            getString(R.string.petweek_title3_text),
            getString(R.string.petweek_title4_text)
        )

        val images = listOf(
            R.drawable.ic_ics_petweek,
            R.drawable.img_pet_option,
            R.drawable.img_pet_caution,
            R.drawable.img_pet_adoption,
        )

        val texts = listOf(
            getString(R.string.petweek_content1_text),
            getString(R.string.petweek_content2_text),
            getString(R.string.petweek_content3_text),
            getString(R.string.petweek_content4_text)
        )

        val paws = listOf(
            R.drawable.ic_paws_1,
            R.drawable.ic_paws_2,
            R.drawable.ic_paws_3,
            R.drawable.ic_paws_4,
        )

        val adapter = ViewPageAdapterPetweek(titles,images, texts, paws)
        viewPager.adapter = adapter

        btnBack.setOnClickListener {
            onBackScreen()
        }

    }


    private fun onBackScreen(){
        val goHomeScreen = Intent(this, HomeActivity::class.java)
        startActivity(goHomeScreen)
    }
}