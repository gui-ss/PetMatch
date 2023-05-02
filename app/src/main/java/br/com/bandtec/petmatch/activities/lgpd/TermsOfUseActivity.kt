package br.com.bandtec.petmatch.activities.lgpd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.aboutapp.AboutAppActivity
import br.com.bandtec.petmatch.activities.home.HomeActivity

class TermsOfUseActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_use)

        btnBack = findViewById(R.id.arrowBack)

        btnBack.setOnClickListener {
            onBackScreen()
        }

    }

    private fun onBackScreen(){
        val goHomeScreen = Intent(this, AboutAppActivity::class.java)
        startActivity(goHomeScreen)
    }

}