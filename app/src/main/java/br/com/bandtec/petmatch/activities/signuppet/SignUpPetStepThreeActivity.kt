package br.com.bandtec.petmatch.activities.signuppet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.data.Endpoint
import br.com.bandtec.petmatch.data.NetworkUtils
import br.com.bandtec.petmatch.data.PetRemoteDataSource
import br.com.bandtec.petmatch.data.model.Pet
import br.com.bandtec.petmatch.data.model.Vacina
import br.com.bandtec.petmatch.presentation.PetPresenter
import kotlinx.android.synthetic.main.activity_sign_up_pet_step_three.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpPetStepThreeActivity : AppCompatActivity() {

    private lateinit var btnFinish: Button

    private lateinit var inputNameVaccine: EditText
    private lateinit var inputDataVaccine: EditText
    private lateinit var inputDoseVaccine: EditText

    private lateinit var tvDescriptionVaccines: TextView

    private var idPet = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_pet_step_three)

        btnFinish =  findViewById(R.id.btn_finish_pet)

        inputNameVaccine =  findViewById(R.id.input_name_vaccine)
        inputDataVaccine =  findViewById(R.id.input_date_vaccine)
        inputDoseVaccine =  findViewById(R.id.input_dose_vaccine)

        tvDescriptionVaccines =  findViewById(R.id.tv_description_vaccine)

        img_seta.setOnClickListener {
            onBackScreen()
        }

        btnFinish.setOnClickListener {
            btnFinish.visibility = View.GONE
            btn_loading_signuppet.visibility = View.VISIBLE

            val dataSource: PetRemoteDataSource = PetRemoteDataSource()
            val presenter: PetPresenter = PetPresenter(this, dataSource)
            presenter.addPet(getDataPet())

        }

        btn_add.setOnClickListener {
            addVaccine(getDataVaccine())
        }

        enableButtonAdd()
        getLastId()
    }

    private fun onBackScreen(){
        val goStepTwo = Intent(this, SignUpPetStepTwoActivity::class.java)
        startActivity(goStepTwo)
    }

        private fun getDataPet(): Pet {
            val nickname = intent.getStringExtra("NICKNAME_PET")
            val age = intent.getStringExtra("AGE_PET")
            val sex = intent.getStringExtra("SEX_PET")
            val breed = intent.getStringExtra("BREED_PET")
            val size = intent.getStringExtra("SIZE_PET")
            val behavior = intent.getStringExtra("BEHAVIOR_PET")
            val castrated = intent.getStringExtra("CASTRATED_PET")
            val sociable = intent.getStringExtra("SOCIABLE_PET")
            val specie = intent.getStringExtra("SPECIE_PET")
            val imgPet = intent.getStringExtra("IMG_PET")

        return Pet(
            id = "",
            petNome = nickname!!,
            petIdade = age!!,
            petEspecie = specie!!,
            petRaca = breed!!,
            petPorte = size!!,
            petSexo = sex!!,
            petCastrado = castrated!!,
            petSociavel = sociable!!,
            petComportamento = behavior!!,
            isPetWeek = "false",
            petFotoPerfil = imgPet!!,
            dataEntregaPet = "00/00/0001"
        )
    }


    fun getDataVaccine(): Vacina{
        val nameVaccine = inputNameVaccine.text.toString()
        var dataVaccine = inputDataVaccine.text.toString()
        val doseVaccine = inputDoseVaccine.text.toString().toInt()


        val reString = "[^A-Za-z0-9 ]".toRegex()
        dataVaccine = reString.replace(dataVaccine, "")

        return Vacina(
            "",
            nameVaccine,
            dataVaccine,
            doseVaccine,
            idPet
        )
    }

    fun showError(code: Int?){
        if (code == 200 || code == 201){
            val finishSignUpPet = Intent(this, SignUpPetSuccessActivity::class.java)
            startActivity(finishSignUpPet)
            finish()
        }else{
            val finishSignUpPet = Intent(this, SignUpPetErrorActivity::class.java)
            startActivity(finishSignUpPet)
            finish()
        }
    }

    fun addVaccine(vacina: Vacina){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .addVaccine(vacina)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                    tvDescriptionVaccines.text = "${inputNameVaccine.text} | ${inputDataVaccine.text} | ${inputDoseVaccine.text} - Adicionada"
                    inputNameVaccine.setText("")
                    inputDataVaccine.setText("")
                    inputDoseVaccine.setText("")

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(baseContext, "Falha ao adicionar vacina", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun enableButtonAdd(){
        inputNameVaccine.doOnTextChanged { _, _, _, _ ->
            validateInputs()
        }

        inputDoseVaccine.doOnTextChanged { _, _, _, _ ->
            validateInputs()
        }

        inputDataVaccine.doOnTextChanged { _, _, _, _ ->
            validateInputs()
        }
    }

    private fun validateInputs(){
        if (inputNameVaccine.text.isNotEmpty() && inputDoseVaccine.text.isNotEmpty() &&
            inputDataVaccine.text.isNotEmpty()){
            btn_add.setBackgroundResource(R.drawable.button_background)
            btn_add.setTextColor(resources.getColor(R.color.white))
            btn_add.isEnabled = true
        }else{
            btn_add.setBackgroundResource(R.drawable.button_background_disable)
            btn_add.setTextColor(resources.getColor(R.color.grey_7))
            btn_add.isEnabled = false
        }
    }

    fun getLastId(){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .searchPetLastId()
            .enqueue(object : Callback <Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    response.body()?.let {
                        idPet = response.body()!!
                    }
                }
                override fun onFailure(call: Call<Int>, t: Throwable) {
                }

            })
    }
}


