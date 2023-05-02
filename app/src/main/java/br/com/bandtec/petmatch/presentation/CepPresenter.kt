package br.com.bandtec.petmatch.presentation

import android.util.Log
import br.com.bandtec.petmatch.activities.signup.SignUpStepThreeActivity
import br.com.bandtec.petmatch.data.CepRemoteDataSource
import br.com.bandtec.petmatch.data.model.Cep

class CepPresenter(signUpStepThreeActivity: SignUpStepThreeActivity, data: CepRemoteDataSource):
    CepRemoteDataSource.CepCallback{

    var view: SignUpStepThreeActivity = signUpStepThreeActivity
    var dataSource: CepRemoteDataSource = data

    fun findCepBy(cep: String) {
        this.dataSource.findCepBy(this, cep)
    }

    override fun onSucess(response: Cep?) {
        if (response != null){
            view.showCep(response)
        }
    }

    override fun onError(message: String?) {
        if (message != null){
            Log.d("erroCep", message)
        }
    }

    override fun onComplete() {
    }

}