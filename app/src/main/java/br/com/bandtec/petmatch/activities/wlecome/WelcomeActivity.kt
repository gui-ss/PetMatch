package br.com.bandtec.petmatch.activities.wlecome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.signin.SignInActivity
import br.com.bandtec.petmatch.activities.signup.SignUpStepOneActivity
import org.w3c.dom.Text

class WelcomeActivity : AppCompatActivity() {

    private lateinit var btnSignUp: Button
    private lateinit var btnSignIn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btnSignIn = findViewById(R.id.btn_signin)
        btnSignUp = findViewById(R.id.btn_signup)

        btnSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpStepOneActivity::class.java)
            startActivity(intent)
        }

    }

}