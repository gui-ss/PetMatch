package br.com.bandtec.petmatch.presentation

import br.com.bandtec.petmatch.activities.home.HomeActivity
import br.com.bandtec.petmatch.activities.pet.carouselcardpets.CarouselCardPetActivity
import br.com.bandtec.petmatch.data.FilterRemoteDataSource
import br.com.bandtec.petmatch.data.model.Filter
import br.com.bandtec.petmatch.data.model.Pet

class FilterPresenter (carouselCardPetActivity: CarouselCardPetActivity, data: FilterRemoteDataSource): FilterRemoteDataSource.FilterCallback {

    var view: CarouselCardPetActivity = carouselCardPetActivity
    var dataSource: FilterRemoteDataSource = data

    fun filterPet(petFilter: Filter){
        this.dataSource.filterPet(this, petFilter)
    }

    override fun onError(message: String?) {
    }

    override fun onComplete() {
    }

    override fun onSuccessFilter(response: List<Pet>?) {
        view.getPets(response)
    }
}