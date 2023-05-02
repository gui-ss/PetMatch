package br.com.bandtec.petmatch.activities.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity
import br.com.bandtec.petmatch.data.CepAPI
import br.com.bandtec.petmatch.data.Endpoint
import br.com.bandtec.petmatch.data.NetworkUtils
import br.com.bandtec.petmatch.data.model.Cep
import br.com.bandtec.petmatch.data.model.User
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.img_preview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnSave: Button
    private lateinit var btnNewImage: ImageView

    private lateinit var inputNome: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var inputPhone: EditText
    private lateinit var inputCep: EditText
    private lateinit var inputStreet: EditText
    private lateinit var inputNeighbourhood: EditText
    private lateinit var inputCity: EditText
    private lateinit var inputUf: EditText

    private lateinit var userUpdate: User

    private lateinit var idUser: String
    private lateinit var ageUser: String
    private lateinit var nicknameUser: String
    private lateinit var sexUser: String
    private lateinit var cpfUser: String
    private lateinit var pictureUser: String

    private var checkDataPet = 0

    private var check = false
    var stringImage: String = ""
    var newStringImage: String? = null

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
        setContentView(R.layout.activity_profile)

        inputNome = findViewById(R.id.input_nomeCompleto)
        inputEmail = findViewById(R.id.input_email)
        inputPassword = findViewById(R.id.input_password)
        inputPhone = findViewById(R.id.input_phone)
        inputCep = findViewById(R.id.input_cep)
        inputStreet = findViewById(R.id.input_street)
        inputNeighbourhood = findViewById(R.id.input_neighbourhood)
        inputCity = findViewById(R.id.input_city)
        inputUf = findViewById(R.id.input_uf)

        btnBack = findViewById<ImageView>(R.id.arrowBack)
        btnNewImage = findViewById<ImageView>(R.id.btn_new_image)

        btnSave = findViewById(R.id.btn_save)

        btnBack.setOnClickListener {
            onBackScreen()
        }

        btnSave.setOnClickListener {
            btn_save.visibility = View.GONE
            btn_loading_profile.visibility = View.VISIBLE
            sendDataUserUpdate()
        }

        btnNewImage.setOnClickListener {
            openGalleryForImage()
        }

        val preferencesId: SharedPreferences = getSharedPreferences("id_user_logged", Context.MODE_PRIVATE)
        if (preferencesId.contains("id_user_logged")) {
            val idUser = preferencesId.getString("id_user_logged", "")
            getUser(idUser!!)
        }else{
            Toast.makeText(baseContext, "Ocorreu um erro, por favor, faça login novamente.", Toast.LENGTH_SHORT).show()
        }

        inputCep.doOnTextChanged { _, _, _, _ ->
            changeCep()
        }

        initPermissions()
    }

    private fun onBackScreen(){
        val goHomeScreen = Intent(this, HomeActivity::class.java)
        startActivity(goHomeScreen)
    }


    fun getUser(id: String){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .getUser(id = id)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    //Picasso.get().load(response.body()?.usuario_foto_perfil).resize(1550, 1000).into(img_preview)
                    decode(response.body()?.usuario_foto_perfil!!)
                    inputNome.setText(response.body()?.usuario_nome)
                    inputEmail.setText(response.body()?.usuario_email)
                    inputPassword.setText(response.body()?.usuario_senha)
                    inputPhone.setText(response.body()?.usuario_telefone)
                    inputCep.setText(response.body()?.usuario_cep)

                    idUser = response.body()?.id.toString()
                    ageUser = response.body()?.usuario_idade!!
                    nicknameUser = response.body()?.usuario_apelido!!
                    sexUser = response.body()?.usuario_sexo!!
                    cpfUser = response.body()?.usuario_cpf!!
                    pictureUser = response.body()?.usuario_foto_perfil!!

                    findCepBy(response.body()?.usuario_cep!!)
                    checkDataPet = 1
                    checkLoading()

                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(baseContext, "Dados indisponiveis, por favor, tente novamente.", Toast.LENGTH_SHORT).show()
                    checkDataPet = 2
                    checkLoading()
                }

            })
    }

    private fun changeCep(){
        findCepBy(inputCep.text.toString())
    }

    fun findCepBy(cep: String){
        NetworkUtils.retrofitCep().create(CepAPI::class.java)
            .findCep(cep = cep)
            .enqueue(object : Callback<Cep> {
                override fun onFailure(call: Call<Cep>, t: Throwable) {
                    Toast.makeText(baseContext, "Erro", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Cep>, response: Response<Cep>) {
                    inputCity.setText(response.body()?.city)
                    inputStreet.setText(response.body()?.street)
                    inputNeighbourhood.setText(response.body()?.neighborhood)
                    inputUf.setText(response.body()?.state)
                }

            })
    }

    private fun sendDataUserUpdate(){
        if (stringImage.isBlank()){
            newStringImage = pictureUser
        }else{
            newStringImage = stringImage
        }

        userUpdate = User(
            idUser.toInt(),
            inputNome.text.toString(),
            ageUser,
            nicknameUser,
            inputCep.text.toString(),
            inputPhone.text.toString(),
            sexUser,
            inputEmail.text.toString(),
            cpfUser,
            newStringImage!!,
            inputPassword.text.toString(),
        )

        alterUser(userUpdate)
    }

    fun alterUser(user: User){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .alterUser(user)
            .enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    Toast.makeText(baseContext, "Informações alteradas com sucesso!", Toast.LENGTH_SHORT).show()
                    finish();
                    startActivity(intent);
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(baseContext, "Não foi possivel alterar as informações.", Toast.LENGTH_SHORT).show()
                }

            })
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

        img_preview.setImageBitmap(decodedImage)
    }

    private fun checkLoading(){
        if (checkDataPet == 2){
            animationViewProfile.visibility = View.VISIBLE
        }else{
            scrollViewProfile.visibility = View.VISIBLE
            animationViewProfile.visibility = View.GONE
        }
    }
}