package br.com.bandtec.petmatch.presentation

import br.com.bandtec.petmatch.activities.home.HomeActivity
import br.com.bandtec.petmatch.activities.pet.carouselcardpets.CarouselCardPetActivity
import br.com.bandtec.petmatch.activities.signuppet.SignUpPetStepThreeActivity
import br.com.bandtec.petmatch.data.PetRemoteDataSource
import br.com.bandtec.petmatch.data.model.Pet

class PetPresenter(signUpActivity: SignUpPetStepThreeActivity, data: PetRemoteDataSource): PetRemoteDataSource.PetCallback {

    var view: SignUpPetStepThreeActivity = signUpActivity
    var viewCarousel: CarouselCardPetActivity = CarouselCardPetActivity()
    var viewFilter: HomeActivity = HomeActivity()
    var dataSource: PetRemoteDataSource = data

    fun findPets(){
        this.dataSource.findPets(this)
    }

    fun addPet(pet: Pet){
        this.dataSource.addPet(this, pet)
    }

    override fun onSuccess(response: List<Pet?>) {
        if (response != null){
            view.showError(201)
        }
    }

    override fun onError(message: String?) {
        if (message != null){
            view.showError(500)
        }
    }

    override fun onComplete() {
    }

    override fun onSuccessRegister(response: Pet?) {
        if (response != null){
            view.showError(200)
        }
    }
    override fun onErrorRegister(code: Int?) {
        view.showError(code)
    }

}