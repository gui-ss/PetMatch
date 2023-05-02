package br.com.bandtec.petmatch.activities.signuppet

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.home.HomeActivity
import kotlinx.android.synthetic.main.activity_sign_up_pet_step_one.*
import kotlinx.android.synthetic.main.activity_sign_up_pet_step_two.*
import kotlinx.android.synthetic.main.activity_sign_up_pet_step_two.img_seta

class SignUpPetStepTwoActivity : AppCompatActivity() {

    private lateinit var inputBreed: EditText

    private lateinit var inputSize: AutoCompleteTextView
    private lateinit var inputBehavior: AutoCompleteTextView
    private lateinit var inputCastrated: AutoCompleteTextView
    private lateinit var inputSociable: AutoCompleteTextView
    private lateinit var inputSpecie: AutoCompleteTextView

    private lateinit var btnNext: Button


    override fun onResume() {
        super.onResume()

        val specie = resources.getStringArray(R.array.specie_pet)
        val size = resources.getStringArray(R.array.size_pet)
        val behavior = resources.getStringArray(R.array.behavior_pet)
        val castrated = resources.getStringArray(R.array.castrated_pet)
        val sociable = resources.getStringArray(R.array.sociable_pet)

        val arrayAdapterSize = ArrayAdapter(this, R.layout.dropdown_item, size)
        tv_dropdown_size.setAdapter(arrayAdapterSize)

        val arrayAdapterBehavior = ArrayAdapter(this, R.layout.dropdown_item, behavior)
        tv_dropdown_behavior.setAdapter(arrayAdapterBehavior)

        val arrayAdapterCastrated = ArrayAdapter(this, R.layout.dropdown_item, castrated)
        tv_dropdown_castrated.setAdapter(arrayAdapterCastrated)

        val arrayAdapterSociable = ArrayAdapter(this, R.layout.dropdown_item, sociable)
        tv_dropdown_sociable.setAdapter(arrayAdapterSociable)

        val arrayAdapterSpecie = ArrayAdapter(this, R.layout.dropdown_item, specie)
        tv_dropdown_specie.setAdapter(arrayAdapterSpecie)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_pet_step_two)

        img_seta.setOnClickListener {
            onBackScreen()
        }

        inputBreed =  findViewById(R.id.input_breed)
        inputSize =  findViewById(R.id.tv_dropdown_size)
        inputBehavior =  findViewById(R.id.tv_dropdown_behavior)
        inputCastrated =  findViewById(R.id.tv_dropdown_castrated)
        inputSociable =  findViewById(R.id.tv_dropdown_sociable)
        inputSpecie =  findViewById(R.id.tv_dropdown_specie)

        btnNext =  findViewById(R.id.btn_prox)

        btnNext.setOnClickListener {
            goNextScreenAndGetDataPet()
        }

        enableButtonNext()

    }

    private fun onBackScreen(){
        val goStepOone = Intent(this, SignUpPetStepOneActivity::class.java)
        startActivity(goStepOone)
    }

    private fun goNextScreenAndGetDataPet(){
        val nickname = intent.getStringExtra("NICKNAME_PET")
        val age = intent.getStringExtra("AGE_PET")
        val sex = intent.getStringExtra("SEX_PET")
        val imgPet = intent.getStringExtra("IMG_PET")

        val breed = inputBreed.text.toString()
        var size = inputSize.text.toString()
        var behavior = inputBehavior.text.toString()
        var castrated = inputCastrated.text.toString()
        var sociable = inputSociable.text.toString()
        var specie = inputSpecie.text.toString()

        size = if (size == "Pequeno"){
            "P"
        }else if(size == "Médio"){
            "M"
        }else{
            "G"
        }

        castrated = if (castrated == "Sim"){
            "True"
        }else{
            "False"
        }

        sociable = if (sociable == "Sim"){
            "True"
        }else{
            "False"
        }

        val goStepThree = Intent(this, SignUpPetStepThreeActivity::class.java)
        goStepThree.putExtra("NICKNAME_PET", nickname)
        goStepThree.putExtra("AGE_PET", age)
        goStepThree.putExtra("SEX_PET", sex)
        goStepThree.putExtra("BREED_PET", breed)
        goStepThree.putExtra("SIZE_PET", size)
        goStepThree.putExtra("BEHAVIOR_PET", behavior)
        goStepThree.putExtra("CASTRATED_PET", castrated)
        goStepThree.putExtra("SOCIABLE_PET", sociable)
        goStepThree.putExtra("SPECIE_PET", specie)
        goStepThree.putExtra("IMG_PET", imgPet)

        startActivity(goStepThree)
    }

    private fun enableButtonNext(){
        inputBreed.doOnTextChanged { _, _, _, _ ->
            validateInputs()
        }
    }

    private fun validateInputs(){
        if (inputBreed.text.isNotEmpty()){
            btnNext.setBackgroundResource(R.drawable.button_background)
            btnNext.setTextColor(resources.getColor(R.color.white))
            btnNext.isEnabled = true
        }else{
            btnNext.setBackgroundResource(R.drawable.button_background_disable)
            btnNext.setTextColor(resources.getColor(R.color.grey_7))
            btnNext.isEnabled = false
        }
    }

}