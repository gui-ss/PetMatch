package br.com.bandtec.petmatch.activities.lgpd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.signup.SignUpStepFourActivity

class LgpdActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lgpd)

        btnBack =  findViewById(R.id.img_seta)

        btnBack.setOnClickListener {
            onBackScreen()
        }
    }

    private fun onBackScreen(){
        val goStepFour = Intent(this, SignUpStepFourActivity::class.java)

        val name = intent.getStringExtra("NAME_USER")
        val email = intent.getStringExtra("EMAIL_USER")
        val password = intent.getStringExtra("PASSWORD_USER")
        val imgUser = intent.getStringExtra("IMG_USER")
        val cpf = intent.getStringExtra("CPF_USER")
        val phone = intent.getStringExtra("PHONE_USER")
        val date = intent.getStringExtra("DATE_USER")
        val cep = intent.getStringExtra("CEP_USER")

        goStepFour.putExtra("NAME_USER", name)
        goStepFour.putExtra("EMAIL_USER", email)
        goStepFour.putExtra("PASSWORD_USER", password)
        goStepFour.putExtra("IMG_USER", imgUser)
        goStepFour.putExtra("CPF_USER", cpf)
        goStepFour.putExtra("PHONE_USER", phone)
        goStepFour.putExtra("DATE_USER", date)
        goStepFour.putExtra("CEP_USER", cep)

        startActivity(goStepFour)
    }
}