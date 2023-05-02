package br.com.bandtec.petmatch.activities.aboutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity
import br.com.bandtec.petmatch.activities.lgpd.PolicyPrivacyActivity
import br.com.bandtec.petmatch.activities.lgpd.TermsOfUseActivity

class AboutAppActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnTerms: ConstraintLayout
    private lateinit var btnPolicy: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)

        btnBack = findViewById(R.id.arrowBack)
        btnTerms = findViewById(R.id.btn_terms_use)
        btnPolicy = findViewById(R.id.btn_policy)

        btnBack.setOnClickListener {
            onBackScreen()
        }

        btnTerms.setOnClickListener {
            goTermsOfUse()
        }

        btnPolicy.setOnClickListener {
            goPolicyPrivacy()
        }

    }

    private fun goTermsOfUse(){
        val intent = Intent(this, TermsOfUseActivity::class.java)
        startActivity(intent)
    }

    private fun goPolicyPrivacy(){
        val intent = Intent(this, PolicyPrivacyActivity::class.java)
        startActivity(intent)
    }

    private fun onBackScreen(){
        val goHomeScreen = Intent(this, HomeActivity::class.java)
        startActivity(goHomeScreen)
    }
}