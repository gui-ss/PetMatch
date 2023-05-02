package br.com.bandtec.petmatch.activities.signup

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.wlecome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_sign_up_step_one.*

class SignUpStepOneActivity : AppCompatActivity() {

    private lateinit var inputName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText

    private lateinit var btnBack: ImageView
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_step_one)

        inputName =  findViewById(R.id.input_nomeCompleto)
        inputEmail =  findViewById(R.id.input_emailCadastro)
        inputPassword =  findViewById(R.id.input_senhaCadastro)

        btnBack =  findViewById(R.id.img_seta)
        btnNext =  findViewById(R.id.btn_prox)

        btnBack.setOnClickListener {
            onBackScreen()
        }

        btnNext.setOnClickListener {
            goNextScreenAndGetDataUser()
        }

        enableButtonNext()

        showAndHidePassword()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showAndHidePassword(){
        inputPassword.setOnTouchListener(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= inputPassword.right - inputPassword.totalPaddingRight) {
                    if (inputPassword.transformationMethod
                            .equals(PasswordTransformationMethod.getInstance())
                    ) {
                        inputPassword.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                        inputPassword.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_eye_open_password, 0
                        )
                    } else {
                        inputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        inputPassword.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_eye_closed_password, 0
                        )
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun goNextScreenAndGetDataUser(){
        val name = inputName.text.toString()
        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()

            val goStepTwo = Intent(this, SignUpStepTwoActivity::class.java)
            goStepTwo.putExtra("NAME_USER", name)
            goStepTwo.putExtra("EMAIL_USER", email)
            goStepTwo.putExtra("PASSWORD_USER", password)

            startActivity(goStepTwo)
    }

    private fun enableButtonNext(){
        inputName.doOnTextChanged { _, _, _, _ ->
            validateInputs()
        }

        inputEmail.doOnTextChanged { _, _, _, _ ->
            validateInputs()
        }

        inputPassword.doOnTextChanged { _, _, _, _ ->
            validateInputs()

            validatePassword()
        }
    }

    private fun validateInputs(){
        if (inputEmail.text.isNotEmpty() && inputName.text.isNotEmpty() &&
            inputPassword.text.isNotEmpty()){
            btnNext.setBackgroundResource(R.drawable.button_background)
            btnNext.setTextColor(resources.getColor(R.color.white))
            btnNext.isEnabled = true
        }else{
            btnNext.setBackgroundResource(R.drawable.button_background_disable)
            btnNext.setTextColor(resources.getColor(R.color.grey_7))
            btnNext.isEnabled = false
        }
    }

    private fun validatePassword(){
        if(inputPassword.text.length >= 8){
            tv_error_password_register.visibility = View.GONE
        }else{
            tv_error_password_register.visibility = View.VISIBLE
            btnNext.setBackgroundResource(R.drawable.button_background)
            btnNext.setTextColor(resources.getColor(R.color.white))
            btnNext.isEnabled = true
        }
    }

    private fun onBackScreen(){
        val goWelcomeScreen = Intent(this, WelcomeActivity::class.java)
        startActivity(goWelcomeScreen)
    }
}