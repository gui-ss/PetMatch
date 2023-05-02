package br.com.bandtec.petmatch.activities.signin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity
import br.com.bandtec.petmatch.activities.wlecome.WelcomeActivity
import br.com.bandtec.petmatch.data.UserRemoteDataSource
import br.com.bandtec.petmatch.data.model.User
import br.com.bandtec.petmatch.presentation.UserPresenter
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    private lateinit var btnSign: Button
    private lateinit var btnBack: TextView

    private lateinit var edEmail: EditText
    private lateinit var edPassword: EditText

    private lateinit var inputEmail: String
    private lateinit var inputPassword: String

    private lateinit var tvUserError: TextView

    private var idUserLogged: String? = null
    private var nameUserLogged: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btnSign = findViewById(R.id.btn_signin)
        btnBack = findViewById(R.id.tv_back)

        edEmail = findViewById(R.id.input_email)
        edPassword = findViewById(R.id.input_password)

        tvUserError = findViewById(R.id.tv_error_signin)

        btnBack.setOnClickListener {
           onBackScreen()
        }

        btnSign.setOnClickListener {
            btnSign.visibility = View.GONE
            btn_loading.visibility = View.VISIBLE
            inputEmail = edEmail.text.toString()
            inputPassword = edPassword.text.toString()
            val dataSource: UserRemoteDataSource = UserRemoteDataSource()
            val presenter: UserPresenter = UserPresenter(this, dataSource)
            presenter.userLogin(inputEmail, inputPassword)
        }

        enableButtonSignIn()

        showAndHidePassword()
    }

    private fun setIdUser(preferences: SharedPreferences){
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putBoolean("user_logged", true)
        editor.putString("name_user_logged", nameUserLogged)
        editor.putString("id_user_logged", idUserLogged)
        editor.apply()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showAndHidePassword(){
        edPassword.setOnTouchListener(OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= edPassword.right - edPassword.totalPaddingRight) {
                    if (edPassword.transformationMethod
                            .equals(PasswordTransformationMethod.getInstance())
                    ){
                        edPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        edPassword.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0,R.drawable.ic_eye_open_password,0)
                    }else{
                        edPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        edPassword.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0,R.drawable.ic_eye_closed_password,0)
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun enableButtonSignIn(){
        edEmail.doOnTextChanged{_, _, _, _ ->
            validateInputs()
        }

        edPassword.doOnTextChanged { _, _, _, _ ->
            validateInputs()
        }
    }

    private fun validateInputs(){
        if (edEmail.text.isNotEmpty() && edPassword.text.isNotEmpty()){
            btnSign.setBackgroundResource(R.drawable.button_background)
            btnSign.setTextColor(resources.getColor(R.color.white))
            btnSign.isEnabled = true
        }else{
            btnSign.setBackgroundResource(R.drawable.button_background_disable)
            btnSign.setTextColor(resources.getColor(R.color.grey_7))
            btnSign.isEnabled = false
        }
    }

    private fun onBackScreen(){
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    fun showFailure(message: String){
        tvUserError.text = "O aplicativo está fora do ar no momento,\npor favor tente novamente mais tarde."
        Log.e("ErroLogin", message)
        btnSign.setBackgroundResource(R.drawable.button_background_disable)
        btnSign.setTextColor(resources.getColor(R.color.grey_7))
        btnSign.isEnabled = false
    }

    fun showErrorLogin(){
        tvUserError.visibility = View.VISIBLE

        btnSign.visibility = View.VISIBLE
        btn_loading.visibility = View.GONE

        btnSign.setBackgroundResource(R.drawable.button_background_disable)
        btnSign.setTextColor(resources.getColor(R.color.grey_7))
        btnSign.isEnabled = false
    }

    fun getIdUserLogged(user: User){
        idUserLogged = user.id.toString()
        nameUserLogged = user.usuario_nome
    }


    fun userLogin(code: Int?){
        if (code == 200){
            val preferences: SharedPreferences = getSharedPreferences("user_logged", Context.MODE_PRIVATE)
            val preferencesName: SharedPreferences = getSharedPreferences("name_user_logged", Context.MODE_PRIVATE)
            val preferencesId: SharedPreferences = getSharedPreferences("id_user_logged", Context.MODE_PRIVATE)
            setIdUser(preferences)
            setIdUser(preferencesName)
            setIdUser(preferencesId)

            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("user_name", nameUserLogged)
            startActivity(intent)
            finish()
        }else{
            showErrorLogin()
        }
    }


}