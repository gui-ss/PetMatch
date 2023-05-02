package br.com.bandtec.petmatch.activities.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.error.ErrorActivity
import br.com.bandtec.petmatch.activities.lgpd.LgpdActivity
import br.com.bandtec.petmatch.activities.success.SuccessActivity
import br.com.bandtec.petmatch.data.UserRemoteDataSource
import br.com.bandtec.petmatch.data.model.User
import br.com.bandtec.petmatch.presentation.UserPresenter
import br.com.bandtec.petmatch.presentation.UserSignUpPresenter
import kotlinx.android.synthetic.main.activity_sign_up_step_four.*

class SignUpStepFourActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnNext: Button
    private lateinit var btnLgpd: TextView

    private lateinit var cbLgpd: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_step_four)

        cbLgpd =  findViewById(R.id.checkbox_lgpdcheckbox_lgpd)

        btnBack =  findViewById(R.id.img_seta)
        btnNext =  findViewById(R.id.btn_finish)
        btnLgpd =  findViewById(R.id.tv_text_lgpd)

        btnBack.setOnClickListener {
            onBackScreen()
        }

        val name = intent.getStringExtra("NAME_USER")
        val email = intent.getStringExtra("EMAIL_USER")
        val password = intent.getStringExtra("PASSWORD_USER")
        val imgUser = intent.getStringExtra("IMG_USER")
        val cpf = intent.getStringExtra("CPF_USER")
        val phone = intent.getStringExtra("PHONE_USER")
        val date = intent.getStringExtra("DATE_USER")
        val cep = intent.getStringExtra("CEP_USER")
        val nickname = intent.getStringExtra("NAME_USER")
        val genre = "x"

        btnNext.setOnClickListener {
            val newUser = User(
                null,
                name!!,
                date!!,
                nickname!!,
                cep!!,
                phone!!,
                genre,
                email!!,
                cpf!!,
                imgUser!!,
                password!!
            )

            btnNext.visibility = View.GONE
            btn_loading_signup.visibility = View.VISIBLE

            val dataSource: UserRemoteDataSource = UserRemoteDataSource()
            val presenter: UserSignUpPresenter = UserSignUpPresenter(this, dataSource)
            presenter.addUser(newUser)
        }

        btnLgpd.setOnClickListener {
            openLgpdScreen()
        }

        cbLgpd.setOnClickListener {
            enableButtonFinish()
        }

    }


    private fun enableButtonFinish(){
        if (cbLgpd.isChecked){
                btnNext.setBackgroundResource(R.drawable.button_background)
                btnNext.setTextColor(resources.getColor(R.color.white))
                btnNext.isEnabled = true
            }else{
                btnNext.setBackgroundResource(R.drawable.button_background_disable)
                btnNext.setTextColor(resources.getColor(R.color.grey_7))
                btnNext.isEnabled = false
            }
    }

    private fun onBackScreen(){
        val goStepOne = Intent(this, SignUpStepOneActivity::class.java)
        startActivity(goStepOne)
    }

    private fun openLgpdScreen(){
        val goLgpd = Intent(this, LgpdActivity::class.java)

        val name = intent.getStringExtra("NAME_USER")
        val email = intent.getStringExtra("EMAIL_USER")
        val password = intent.getStringExtra("PASSWORD_USER")
        val imgUser = intent.getStringExtra("IMG_USER")
        val cpf = intent.getStringExtra("CPF_USER")
        val phone = intent.getStringExtra("PHONE_USER")
        val date = intent.getStringExtra("DATE_USER")
        val cep = intent.getStringExtra("CEP_USER")

        goLgpd.putExtra("NAME_USER", name)
        goLgpd.putExtra("EMAIL_USER", email)
        goLgpd.putExtra("PASSWORD_USER", password)
        goLgpd.putExtra("IMG_USER", imgUser)
        goLgpd.putExtra("CPF_USER", cpf)
        goLgpd.putExtra("PHONE_USER", phone)
        goLgpd.putExtra("DATE_USER", date)
        goLgpd.putExtra("CEP_USER", cep)

        startActivity(goLgpd)
    }

    fun showError(code: Int?){
        if (code == 200){
            val finishSignUp = Intent(this, SuccessActivity::class.java)
            startActivity(finishSignUp)
            finish()
        }else{
            val finishSignUp = Intent(this, ErrorActivity::class.java)
            startActivity(finishSignUp)
            finish()
        }
    }

}