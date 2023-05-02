package br.com.bandtec.petmatch.activities.help

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity
import kotlinx.android.synthetic.main.activity_help.*
import android.os.Bundle as Bundle1


class HelpActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle1?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        btnBack = findViewById(R.id.arrowBackHelp)

        btnBack.setOnClickListener {
            onBackScreen()
        }
        val btnCopy = findViewById<View>(R.id.copy_btn)
        btnCopy.setOnClickListener{
            copyText()
        }

    }

    private fun copyText(){
        val texto = findViewById<TextView>(R.id.email_support).text.toString()
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("E-mail copiado", texto)
        val copied = getString(R.string.help_copied_text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, copied, Toast.LENGTH_SHORT).show()
    }

    private fun onBackScreen(){
        val goHomeScreen = Intent(this, HomeActivity::class.java)
        startActivity(goHomeScreen)
    }
}