package br.com.bandtec.petmatch.activities.signuppet

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_sign_up_pet_step_one.*
import kotlinx.android.synthetic.main.activity_sign_up_pet_step_one.btn_prox
import kotlinx.android.synthetic.main.activity_sign_up_pet_step_one.img_seta
import java.io.ByteArrayOutputStream

class SignUpPetStepOneActivity : AppCompatActivity() {

    private lateinit var inputNickname: EditText
    private lateinit var inputAge: EditText
    private lateinit var inputSex: AutoCompleteTextView
    private lateinit var imgPreview: CircleImageView

    private lateinit var btnAddImage: ImageView

    private lateinit var btnNext: Button

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_pet_step_one)

        inputNickname =  findViewById(R.id.input_nickname)
        inputAge =  findViewById(R.id.input_age)
        inputSex =  findViewById(R.id.tv_dropdown_sex)
        imgPreview =  findViewById(R.id.img_preview)


        btnAddImage =  findViewById(R.id.btn_new_image)

        btnNext =  findViewById(R.id.btn_prox)

        btnAddImage.setOnClickListener {
            openGalleryForImage()
        }

        img_seta.setOnClickListener {
            onBackScreen()
        }

        btn_prox.setOnClickListener {
            goNextScreenAndGetDataPet()

        }

        enableButtonNext()

        initPermissions()
    }

    override fun onResume() {
        super.onResume()

        val sex = resources.getStringArray(R.array.sex_pet)

        val arrayAdapterSize = ArrayAdapter(this, R.layout.dropdown_item, sex)
        tv_dropdown_sex.setAdapter(arrayAdapterSize)

    }

    private fun goNextScreenAndGetDataPet(){
        val nickname = inputNickname.text.toString()
        val age = inputAge.text.toString()
        var sex = inputSex.text.toString()

        sex = if (sex == "Macho"){
            "M"
        }else{
            "F"
        }

        val goStepTwo = Intent(this, SignUpPetStepTwoActivity::class.java)
        goStepTwo.putExtra("NICKNAME_PET", nickname)
        goStepTwo.putExtra("AGE_PET", age)
        goStepTwo.putExtra("SEX_PET", sex)
        goStepTwo.putExtra("IMG_PET", stringImage)

        startActivity(goStepTwo)
    }

    private fun onBackScreen(){
        val goHome = Intent(this, HomeActivity::class.java)
        startActivity(goHome)
    }

    private fun enableButtonNext(){
        inputNickname.doOnTextChanged { _, _, _, _ ->
                validateInputs()
        }

        inputAge.doOnTextChanged { _, _, _, _ ->
            validateInputs()
        }

        inputSex.doOnTextChanged { _, _, _, _ ->
            validateInputs()
        }
    }

    private fun validateInputs(){
        if (inputNickname.text.isNotEmpty() && inputAge.text.isNotEmpty() &&
            inputSex.text.isNotEmpty() && stringImage.isNullOrBlank().not()){
            btnNext.setBackgroundResource(R.drawable.button_background)
            btnNext.setTextColor(resources.getColor(R.color.white))
            btnNext.isEnabled = true
        }else{
            btnNext.setBackgroundResource(R.drawable.button_background_disable)
            btnNext.setTextColor(resources.getColor(R.color.grey_7))
            btnNext.isEnabled = false
        }
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
        //val bit = imageString.toByteArray()
        //val bitmap = BitmapFactory.decodeByteArray(bit, 0, bit.size)

        imgPreview.setImageBitmap(decodedImage)
    }
}