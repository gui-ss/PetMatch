package br.com.bandtec.petmatch.activities.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import br.com.bandtec.petmatch.R
import br.com.bandtec.petmatch.activities.aboutapp.AboutAppActivity
import br.com.bandtec.petmatch.activities.help.HelpActivity
import br.com.bandtec.petmatch.activities.match.MatchActivity
import br.com.bandtec.petmatch.activities.pet.carouselcardpets.CarouselCardPetActivity
import br.com.bandtec.petmatch.activities.pet.mypets.MyPetsActivity
import br.com.bandtec.petmatch.activities.pet.mypetsliked.MyPetsLikedActivity
import br.com.bandtec.petmatch.activities.pet.petsliked.PetsLikedActivity
import br.com.bandtec.petmatch.activities.petweek.PetWeekActivity
import br.com.bandtec.petmatch.activities.profile.ProfileActivity
import br.com.bandtec.petmatch.activities.signuppet.SignUpPetStepOneActivity
import br.com.bandtec.petmatch.activities.wlecome.WelcomeActivity
import br.com.bandtec.petmatch.data.model.Filter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up_pet_step_one.*
import kotlinx.android.synthetic.main.activity_sign_up_pet_step_two.*
import kotlinx.android.synthetic.main.layout_filter_bottom_sheet.*


class HomeActivity : AppCompatActivity() {

    private lateinit var btnMenu: ImageView

    private lateinit var btnSearchPet: ConstraintLayout

    private lateinit var btnSignOut: ConstraintLayout

    private lateinit var btnMyPets: ConstraintLayout

    private lateinit var btnMenuMyPets: ConstraintLayout

    private lateinit var btnMenuPetweek: ConstraintLayout

    private lateinit var btnMenuAboutApp: ConstraintLayout

    private lateinit var btnProfile: ConstraintLayout

    private lateinit var btnPetsLiked: ConstraintLayout

    private lateinit var btnMenuHelp: ConstraintLayout

    private lateinit var btnMenuMyPetsLiked: ConstraintLayout

    private lateinit var btnMenuPetsLiked: ConstraintLayout

    private lateinit var btnMenuMatchs: ConstraintLayout


    private lateinit var inputFilterNickname: EditText
    private lateinit var inputFilterAge: EditText
    private lateinit var inputFilterBreed: EditText
    private lateinit var inputFilterBehavior: EditText
    private lateinit var inputFilterSize: AutoCompleteTextView
    private lateinit var inputFilterSex: AutoCompleteTextView
    private lateinit var inputFilterCastrated: AutoCompleteTextView
    private lateinit var inputFilterSociable: AutoCompleteTextView

    private var radioGroupFilterSpecie: RadioGroup? = null

    private lateinit var dataPetFilter: Filter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnMenu = findViewById(R.id.menu)
        btnSearchPet = findViewById(R.id.btn_search_pets)
        btnSignOut = findViewById(R.id.btn_menu_leave)
        btnMyPets = findViewById(R.id.btn_my_pets)
        btnMenuMyPets = findViewById(R.id.btn_menu_my_pets)
        btnMenuPetweek = findViewById(R.id.btn_menu_petweek)
        btnMenuAboutApp = findViewById(R.id.btn_menu_about_app)
        btnProfile = findViewById(R.id.btn_menu_my_profile)
        btnMenuHelp = findViewById(R.id.btn_menu_help)
        btnMenuMyPetsLiked = findViewById(R.id.btn_menu_my_liked_pets)
        btnMenuMatchs = findViewById(R.id.btn_menu_match)
        btnMenuPetsLiked = findViewById(R.id.btn_menu_liked_pets)
        btnPetsLiked = findViewById(R.id.btn_liked_pets)

        btnMenu.setOnClickListener {
           openMenu()
        }

        btnSearchPet.setOnClickListener {
            showBottomSheetDialog()
        }

        btnSignOut.setOnClickListener {
            signOut()
        }

        changeNameWelcome()
        btnMyPets.setOnClickListener {
            openMyPets()
        }

        btnMenuMyPets.setOnClickListener {
            openMyPets()
        }

        btnMenuPetweek.setOnClickListener {
            openPetweek()
        }

        btnMenuAboutApp.setOnClickListener {
            openAboutApp()
        }

        btnProfile.setOnClickListener {
            openMyProfile()
        }

        btnMenuHelp.setOnClickListener {
            openHelp()
        }

        btnMenuMyPetsLiked.setOnClickListener {
            openMyPetsLiked()
        }

        btnMenuMatchs.setOnClickListener {
            openMatch()
        }

        btn_new_pet.setOnClickListener {
            openSignUpPet()
        }

        btnMenuPetsLiked.setOnClickListener {
            openPetsLiked()
        }

        btnPetsLiked.setOnClickListener {
            openPetsLiked()
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun signOut(){
        clearStoredData(baseContext)
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun clearStoredData(context: Context) {
        val editor = context.getSharedPreferences("user_logged", 0).edit()
        editor.clear()
        editor.apply()
    }

    @SuppressLint("WrongConstant")
    private fun openMenu(){
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        drawerLayout.openDrawer(Gravity.START)
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(
            this@HomeActivity, R.style.BottomSheetDialogTheme
        )

        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.layout_filter_bottom_sheet,
            findViewById<ConstraintLayout>(R.id.filterBottomSheet)
        )

        bottomSheetDialog.setContentView(bottomSheetView)

        bottomSheetDialog.findViewById<Button>(R.id.btn_search)?.setOnClickListener {
            getDataFilter()
        }

        bottomSheetDialog.show()

        inputFilterNickname = bottomSheetDialog.findViewById(R.id.edNicknameFilter)!!
        inputFilterAge = bottomSheetDialog.findViewById(R.id.edAgeFilter)!!
        inputFilterBreed = bottomSheetDialog.findViewById(R.id.edBreedFilter)!!
        inputFilterBehavior = bottomSheetDialog.findViewById(R.id.tv_dropdown_behavior_filter)!!
        inputFilterSize = bottomSheetDialog.findViewById(R.id.tv_dropdown_size_filter)!!
        inputFilterSex = bottomSheetDialog.findViewById(R.id.tv_dropdown_sex_filter)!!
        inputFilterCastrated = bottomSheetDialog.findViewById(R.id.tv_dropdown_castrated_filter)!!
        inputFilterSociable = bottomSheetDialog.findViewById(R.id.tv_dropdown_sociable_filter)!!

        radioGroupFilterSpecie = bottomSheetDialog.findViewById(R.id.rg_specie)!!


        val size = resources.getStringArray(R.array.size_pet)
        val sex = resources.getStringArray(R.array.sex_pet)
        val behavior = resources.getStringArray(R.array.behavior_pet)
        val castrated = resources.getStringArray(R.array.castrated_pet)
        val sociable = resources.getStringArray(R.array.sociable_pet)

        val arrayAdapterSex = ArrayAdapter(this, R.layout.dropdown_item, sex)
        bottomSheetDialog.tv_dropdown_sex_filter.setAdapter(arrayAdapterSex)

        val arrayAdapterSize = ArrayAdapter(this, R.layout.dropdown_item, size)
        bottomSheetDialog.tv_dropdown_size_filter.setAdapter(arrayAdapterSize)

        val arrayAdapterBehavior = ArrayAdapter(this, R.layout.dropdown_item, behavior)
        bottomSheetDialog.tv_dropdown_behavior_filter.setAdapter(arrayAdapterBehavior)

        val arrayAdapterCastrated = ArrayAdapter(this, R.layout.dropdown_item, castrated)
        bottomSheetDialog.tv_dropdown_castrated_filter.setAdapter(arrayAdapterCastrated)

        val arrayAdapterSociable = ArrayAdapter(this, R.layout.dropdown_item, sociable)
        bottomSheetDialog.tv_dropdown_sociable_filter.setAdapter(arrayAdapterSociable)

    }

    private fun getDataFilter(){
        val nickname = inputFilterNickname.text.toString()
        val age = inputFilterAge.text.toString()
        val breed = inputFilterBreed.text.toString()
        var behavior = inputFilterBehavior.text.toString()
        var size = inputFilterSize.text.toString()
        var sex = inputFilterSex.text.toString()
        var sociable = inputFilterSociable.text.toString()
        var castrated = inputFilterCastrated.text.toString()

        val specieSelected: Int = radioGroupFilterSpecie!!.checkedRadioButtonId
        var specie: String = "null"

        if (specieSelected == 1){
            specie = "Cachorro"
        }else if(specieSelected == 2){
            specie = "Gato"
        }else{
            specie = "null"
        }

        size = if (size == "Pequeno"){
            "P"
        }else if(size == "Médio"){
            "M"
        }else if(size == "Grande"){
            "G"
        }else{
            "null"
        }

        castrated = if (castrated == "Sim"){
            "true"
        }else if(castrated == "Não"){
            "false"
        }else{
            "null"
        }

        sociable = if (sociable == "Sim"){
            "true"
        }else if (sex == "Não"){
            "false"
        }else{
            "null"
        }

        sex = if (sex == "Macho"){
            "M"
        }else if (sex == "Fêmea"){
            "F"
        }else{
            "null"
        }

        if (behavior == "Comportamento"){
            behavior = "null"
        }

        val goFilter = Intent(this, CarouselCardPetActivity::class.java)
        goFilter.putExtra("NICKNAME_FILTER", nickname)
        goFilter.putExtra("AGE_FILTER", age)
        goFilter.putExtra("BREED_FILTER", breed)
        goFilter.putExtra("BEHAVIOR_FILTER", behavior)
        goFilter.putExtra("SIZE_FILTER", size)
        goFilter.putExtra("SEX_FILTER", sex)
        goFilter.putExtra("SOCIABLE_FILTER", sociable)
        goFilter.putExtra("CASTRATED_FILTER", castrated)
        goFilter.putExtra("SPECIE_FILTER", specie)

        startActivity(goFilter)
    }

    @SuppressLint("StringFormatInvalid")
    fun changeNameWelcome(){
        val preferencesName: SharedPreferences = getSharedPreferences("name_user_logged", Context.MODE_PRIVATE)
        if (preferencesName.contains("name_user_logged")) {
            val nameComplete = preferencesName.getString("name_user_logged", "")
            val firstName = nameComplete?.split(" ")?.get(0)
            textWelcome.text =
                getString(R.string.home_welcome_string,  firstName)
        }
    }

    private fun openMyPets(){
        val intent = Intent(this, MyPetsActivity::class.java)
        startActivity(intent)
    }

    private fun openPetweek(){
        val intent = Intent(this, PetWeekActivity::class.java)
        startActivity(intent)
    }

    private fun openAboutApp(){
        val intent = Intent(this, AboutAppActivity::class.java)
        startActivity(intent)
    }

    private fun openHelp(){
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    private fun openSignUpPet(){
        val intent = Intent(this, SignUpPetStepOneActivity::class.java)
        startActivity(intent)
    }

    private fun openMyProfile(){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun openMyPetsLiked(){
        val intent = Intent(this, MyPetsLikedActivity::class.java)
        startActivity(intent)
    }

    private fun openMatch(){
        val intent = Intent(this, MatchActivity::class.java)
        startActivity(intent)
    }

    private fun openPetsLiked(){
        val intent = Intent(this, PetsLikedActivity::class.java)
        startActivity(intent)
    }
}