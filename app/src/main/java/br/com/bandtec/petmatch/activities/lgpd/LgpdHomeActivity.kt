package br.com.bandtec.petmatch.activities.lgpd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity

class LgpdHomeActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lgpd_home)

        btnBack =  findViewById(R.id.img_seta)

        btnBack.setOnClickListener {
            onBackScreen()
        }
    }

    private fun onBackScreen(){
        val goHome = Intent(this, HomeActivity::class.java)
        startActivity(goHome)
    }
}