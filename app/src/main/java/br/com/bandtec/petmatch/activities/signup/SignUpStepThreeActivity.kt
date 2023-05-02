package br.com.bandtec.petmatch.activities.signup

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.data.CepRemoteDataSource
import br.com.bandtec.petmatch.data.model.Cep
import br.com.bandtec.petmatch.presentation.CepPresenter
import java.io.ByteArrayOutputStream


class SignUpStepThreeActivity : AppCompatActivity() {

    private lateinit var inputCep: EditText
    private lateinit var inputStreet: EditText
    private lateinit var inputNeighbourhood: EditText
    private lateinit var inputCity: EditText
    private lateinit var inputUf: EditText

    private lateinit var btnBack: ImageView
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_step_three)

        inputCep =  findViewById(R.id.input_cep)
        inputStreet =  findViewById(R.id.input_rua)
        inputNeighbourhood =  findViewById(R.id.input_bairro)
        inputCity =  findViewById(R.id.input_cidade)
        inputUf =  findViewById(R.id.input_estado)

        btnBack =  findViewById(R.id.img_seta)
        btnNext =  findViewById(R.id.btn_prox)

        btnBack.setOnClickListener {
            onBackScreen()
        }

        btnNext.setOnClickListener {
            goNextScreenAndGetDataUser()
        }

        enableButtonNext()

        inputCep.doOnTextChanged { _, _, _, _ ->
            val dataSource: CepRemoteDataSource = CepRemoteDataSource()
            val presenter: CepPresenter = CepPresenter(this, dataSource)
            var cep = inputCep.text.toString()

            val reString = "[^A-Za-z0-9 ]".toRegex()
            cep = reString.replace(cep, "")

            if (cep.length == 8) {
                presenter.findCepBy(cep)
            }
        }
    }


    fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }

    private fun goNextScreenAndGetDataUser(){
        var cep = inputCep.text.toString()

        val reString = "[^A-Za-z0-9 ]".toRegex()
        cep = reString.replace(cep, "")

        val name = intent.getStringExtra("NAME_USER")
        val email = intent.getStringExtra("EMAIL_USER")
        val password = intent.getStringExtra("PASSWORD_USER")
        val imgUser = intent.getStringExtra("IMG_USER")
        val cpf = intent.getStringExtra("CPF_USER")
        val phone = intent.getStringExtra("PHONE_USER")
        val date = intent.getStringExtra("DATE_USER")

        val goStepFour = Intent(this, SignUpStepFourActivity::class.java)
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

    private fun enableButtonNext(){
        inputCep.doOnTextChanged { _, _, _, _ ->
            if (inputCep.text.isNotEmpty() && inputCep.text.length == 9){
                btnNext.setBackgroundResource(R.drawable.button_background)
                btnNext.setTextColor(resources.getColor(R.color.white))
                btnNext.isEnabled = true
            }else{
                btnNext.setBackgroundResource(R.drawable.button_background_disable)
                btnNext.setTextColor(resources.getColor(R.color.grey_7))
                btnNext.isEnabled = false

                inputStreet.setText("")
                inputNeighbourhood.setText("")
                inputCity.setText("")
                inputUf.setText("")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun showCep(cep: Cep){
        inputStreet.setText(cep.street)
        inputNeighbourhood.setText(cep.neighborhood)
        inputCity.setText(cep.city)
        inputUf.setText(cep.state)
    }

    private fun onBackScreen(){
        val goStepTwo = Intent(this, SignUpStepTwoActivity::class.java)
        startActivity(goStepTwo)
    }


}