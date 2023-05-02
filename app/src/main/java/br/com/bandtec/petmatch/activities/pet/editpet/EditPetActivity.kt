package br.com.bandtec.petmatch.activities.pet.editpet

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.pet.mypets.MyPetsActivity
import br.com.bandtec.petmatch.data.Endpoint
import br.com.bandtec.petmatch.data.NetworkUtils
import br.com.bandtec.petmatch.data.model.NewPet
import br.com.bandtec.petmatch.data.model.Pet
import kotlinx.android.synthetic.main.activity_about_pet.*
import kotlinx.android.synthetic.main.activity_edit_pet.*
import kotlinx.android.synthetic.main.activity_edit_pet.scrollView
import kotlinx.android.synthetic.main.activity_my_pets.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class EditPetActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnSave: Button
    private lateinit var btnDelete: ImageView

    private lateinit var inputNome: EditText
    private lateinit var inputIdade: EditText
    private lateinit var inputSex: AutoCompleteTextView
    private lateinit var inputBreed: EditText
    private lateinit var inputBehavior: AutoCompleteTextView
    private lateinit var inputCastrated: AutoCompleteTextView
    private lateinit var inputSociable: AutoCompleteTextView

    private lateinit var petUpdate: NewPet

    private lateinit var idPet: String
    private lateinit var speciePet: String
    private lateinit var sizePet: String
    private lateinit var isPetWeek: String
    private lateinit var petFotoPerfil: String
    private lateinit var dataEntregaPet: String

    private var checkDataPet = 0

    private var check = false
    var stringImage: String = ""
    var newStringImage: String? = null
    var oldImage: String? = null

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
        setContentView(R.layout.activity_edit_pet)

        inputNome = findViewById(R.id.input_nomeCompleto)
        inputIdade = findViewById(R.id.input_email)
        inputSex = findViewById(R.id.tv_dropdown_sex)
        inputBreed = findViewById(R.id.input_phone)
        inputBehavior = findViewById(R.id.tv_dropdown_behavior)
        inputCastrated = findViewById(R.id.tv_dropdown_castrated)
        inputSociable = findViewById(R.id.tv_dropdown_sociable)

        btnBack = findViewById(R.id.arrowBack)
        btnSave = findViewById(R.id.btn_save)
        btnDelete = findViewById(R.id.btn_delete)

        btnBack.setOnClickListener {
            onBackScreen()
        }

        btnSave.setOnClickListener {
            sendDataPetUpdate()
            scrollView.visibility = View.GONE
            animationViewEditPet.visibility = View.VISIBLE
        }

        btnDelete.setOnClickListener {
            openDialogDelete()
        }

        btn_new_image.setOnClickListener {
            openGalleryForImage()
        }

        val idSelected = intent.getIntExtra("ID_PET_SELECTED_EDIT", 0)
        getPet(idSelected.toString())


        val sex = resources.getStringArray(R.array.sex_pet)
        val behavior = resources.getStringArray(R.array.behavior_pet)
        val castrated = resources.getStringArray(R.array.castrated_pet)
        val sociable = resources.getStringArray(R.array.sociable_pet)

        val arrayAdapterSex = ArrayAdapter(this, R.layout.dropdown_item, sex)
        tv_dropdown_sex.setAdapter(arrayAdapterSex)

        val arrayAdapterBehavior = ArrayAdapter(this, R.layout.dropdown_item, behavior)
        tv_dropdown_behavior.setAdapter(arrayAdapterBehavior)

        val arrayAdapterCastrated = ArrayAdapter(this, R.layout.dropdown_item, castrated)
        tv_dropdown_castrated.setAdapter(arrayAdapterCastrated)

        val arrayAdapterSociable = ArrayAdapter(this, R.layout.dropdown_item, sociable)
        tv_dropdown_sociable.setAdapter(arrayAdapterSociable)

        initPermissions()
    }

    private fun onBackScreen(){
        val goHomeScreen = Intent(this, MyPetsActivity::class.java)
        startActivity(goHomeScreen)
    }

    fun getPet(id: String){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .getPetById(id = id)
            .enqueue(object : Callback<Pet> {
                override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                    decode(response.body()?.petFotoPerfil!!)
                    inputNome.setText(response.body()?.petNome)
                    inputIdade.setText(response.body()?.petIdade)
                    inputBreed.setText(response.body()?.petRaca)
                    inputBehavior.setText(response.body()?.petComportamento)
                    oldImage = response.body()?.petFotoPerfil

                   if (response.body()?.petCastrado == "true"){
                        inputCastrated.setText("Sim")
                    }else{
                        inputCastrated.setText("Não")
                    }

                    if (response.body()?.petSociavel == "true"){
                        inputSociable.setText("Sim")
                    }else{
                        inputSociable.setText("Não")
                    }

                    if (response.body()?.petSexo == "M"){
                        inputSex.setText("Macho")
                    }else{
                        inputSex.setText("Fêmea")
                    }

                    idPet = response.body()?.id!!
                    speciePet = response.body()?.petEspecie!!
                    sizePet = response.body()?.petPorte!!
                    isPetWeek = response.body()?.isPetWeek!!
                    petFotoPerfil = response.body()?.petFotoPerfil!!
                    dataEntregaPet = response.body()?.dataEntregaPet!!
                    checkDataPet = 1

                    checkLoading()
                }
                override fun onFailure(call: Call<Pet>, t: Throwable) {
                    Toast.makeText(baseContext, "Não foi possivel carregas as informações", Toast.LENGTH_SHORT).show()
                    checkDataPet = 2
                    checkLoading()
                }

            })
    }

    private fun sendDataPetUpdate(){

        if (inputCastrated.text.equals( "Sim")){
            inputCastrated.setText("true")
        }else{
            inputCastrated.setText("false")
        }

        if (inputSociable.text.equals( "Sim")){
            inputSociable.setText("true")
        }else{
            inputSociable.setText("false")
        }

        if (inputSex.text.equals( "Macho")){
            inputSex.setText("M")
        }else{
            inputSex.setText("F")
        }

        petUpdate = NewPet(
            idPet,
            inputNome.text.toString(),
            inputIdade.text.toString(),
            speciePet,
            inputBreed.text.toString(),
            sizePet,
            inputSex.text.toString(),
            inputCastrated.text.toString(),
            inputSociable.text.toString(),
            inputBehavior.text.toString(),
            isPetWeek,
            oldImage!!
        )

        alterPet(petUpdate)

    }

    private fun openDialogDelete(){
        val dialogDeletePet: DialogDeletePet = DialogDeletePet()
        dialogDeletePet.show(supportFragmentManager,"dialogDelete")

        val bundle = bundleOf( "idPetDelete" to idPet)
        dialogDeletePet.arguments = bundle
    }

    fun alterPet(pet: NewPet){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .alterPet(pet)
            .enqueue(object : Callback<NewPet>{
                override fun onResponse(call: Call<NewPet>, response: Response<NewPet>) {
                    Toast.makeText(baseContext, "Informações alteradas com sucesso!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(baseContext, MyPetsActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<NewPet>, t: Throwable) {
                    animationViewErrorMyPets.visibility = View.GONE
                    scrollView.visibility = View.VISIBLE
                    Toast.makeText(baseContext, "Erro", Toast.LENGTH_SHORT).show()
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
            animationViewEditPet.visibility = View.VISIBLE
        }else{
            scrollView.visibility = View.VISIBLE
            animationViewEditPet.visibility = View.GONE
        }
    }
}