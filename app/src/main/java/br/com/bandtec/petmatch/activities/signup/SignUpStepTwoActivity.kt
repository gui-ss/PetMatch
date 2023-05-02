package br.com.bandtec.petmatch.activities.signup

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import br.com.bandtec.petmatch.R
import br.com.colman.simplecpfvalidator.isCpf
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_sign_up_step_two.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.Base64.getDecoder
import java.util.Base64.getEncoder


class SignUpStepTwoActivity : AppCompatActivity() {

    private lateinit var inputCpf: EditText
    private lateinit var inputPhone: EditText
    private lateinit var inputDate: EditText

    private lateinit var imgPreview: CircleImageView
    private lateinit var userImageString: ByteArray

    private lateinit var btnBack: ImageView
    private lateinit var btnNext: Button
    private lateinit var btnAddImage: ImageView

    private var check = false
    var stringImage: String? = null

    private val startImageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            val uriImage = result.data?.data
            if (uriImage != null) {
                stringImage = encode(uriImage)
            }

            stringImage?.let { decode(it) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_step_two)

        inputCpf =  findViewById(R.id.input_cpf)
        inputPhone =  findViewById(R.id.input_celular)
        inputDate =  findViewById(R.id.input_nascimento)

        btnBack =  findViewById(R.id.img_seta)
        btnNext =  findViewById(R.id.btn_prox)
        btnAddImage =  findViewById(R.id.btn_new_image)

        imgPreview =  findViewById(R.id.img_preview)

        btnBack.setOnClickListener {
            onBackScreen()
        }

        btnNext.setOnClickListener {
            goNextScreenAndGetDataUser()
        }

        btnAddImage.setOnClickListener {
            openGalleryForImage()
        }

        enableButtonNext()

        initPermissions()
    }

    private fun goNextScreenAndGetDataUser(){
        var cpf = inputCpf.text.toString()
        var phone = inputPhone.text.toString()
        var date = inputDate.text.toString()

        val name = intent.getStringExtra("NAME_USER")
        val email = intent.getStringExtra("EMAIL_USER")
        val password = intent.getStringExtra("PASSWORD_USER")

        val reString = "[^A-Za-z0-9 ]".toRegex()
        cpf = reString.replace(cpf, "")
        phone = reString.replace(phone, "")
        date = reString.replace(date, "")

        val goStepThree = Intent(this, SignUpStepThreeActivity::class.java)
        goStepThree.putExtra("NAME_USER", name)
        goStepThree.putExtra("EMAIL_USER", email)
        goStepThree.putExtra("PASSWORD_USER", password)
        goStepThree.putExtra("IMG_USER", stringImage)
        goStepThree.putExtra("CPF_USER", cpf)
        goStepThree.putExtra("PHONE_USER", phone)
        goStepThree.putExtra("DATE_USER", calculeAgeUser(date.toInt()))

        startActivity(goStepThree)
    }

    private fun enableButtonNext(){
        inputCpf.doOnTextChanged { _, _, _, _ ->
            if (inputCpf.text.toString().isCpf()) {
                tv_error_cpf.visibility = View.GONE
                validateInputs()
            }else{
                tv_error_cpf.visibility = View.VISIBLE
            }
        }

        inputPhone.doOnTextChanged { _, _, _, _ ->
            validateInputs()
        }

        inputDate.doOnTextChanged { _, _, _, _ ->
                validateInputs()
        }
    }

    private fun validateInputs(){
        if (inputCpf.text.isNotEmpty() && inputPhone.text.isNotEmpty() &&
            inputDate.text.isNotEmpty() && inputCpf.text.length >= 11 &&
            inputPhone.text.length >= 11 && inputDate.text.length >= 7 && stringImage.isNullOrBlank().not()){
                btnNext.setBackgroundResource(R.drawable.button_background)
                btnNext.setTextColor(resources.getColor(R.color.white))
                btnNext.isEnabled = true
        }else{
            btnNext.setBackgroundResource(R.drawable.button_background_disable)
            btnNext.setTextColor(resources.getColor(R.color.grey_7))
            btnNext.isEnabled = false
        }
    }

    private fun calculeAgeUser(currentDate: Int): String{
        val currentAge = currentDate.toString().substring(currentDate.toString().length - 4)
        val total = 2021 - currentAge.toInt()
        return total.toString()
    }

    private fun onBackScreen(){
        val goStepOne = Intent(this, SignUpStepOneActivity::class.java)
        startActivity(goStepOne)
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startImageResult.launch(intent)
    }

    private fun initPermissions(){
        if(!getPermission()) setPermission()
        else check = true
    }

    private fun getPermission(): Boolean =
        (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

    private fun setPermission(){
        val permissionsList = listOf<String>(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(this, permissionsList.toTypedArray(), PERMISSION_CODE)
    }

    private fun errorPermission(){
        Toast.makeText(this,"Sem permissão", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    errorPermission()
                } else {
                    check = true
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val PERMISSION_CODE = 1
    }


    fun encode(imageUri: Uri): String {
        val input = contentResolver.openInputStream(imageUri)
        val image = BitmapFactory.decodeStream(input , null, null)
        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes: ByteArray = baos.toByteArray()

        val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        return imageString
    }

    fun decode(imageString: String) {
        val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        imgPreview.setImageBitmap(decodedImage)
    }

}