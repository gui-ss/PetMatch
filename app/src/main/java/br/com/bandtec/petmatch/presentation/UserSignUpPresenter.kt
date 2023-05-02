package br.com.bandtec.petmatch.presentation

import br.com.bandtec.petmatch.activities.signup.SignUpStepFourActivity
import br.com.bandtec.petmatch.data.UserRemoteDataSource
import br.com.bandtec.petmatch.data.model.User

class UserSignUpPresenter(signUpStepFourActivity: SignUpStepFourActivity, data: UserRemoteDataSource):
    UserRemoteDataSource.UserCallback  {

    var view: SignUpStepFourActivity = signUpStepFourActivity
    var dataSource: UserRemoteDataSource = data

    fun addUser(user: User){
        this.dataSource.addUser(this, user)
    }

    override fun onSuccess(response: User?) {
        if (response != null) {
            view.showError(200)
        }
    }

    override fun onError(message: String?) {
        if (message != null){
            view.showError(500)
        }
    }

    override fun onErrorLogin(code: Int?) {

    }

    override fun onErrorRegister(code: Int?) {
        view.showError(code)
    }

    override fun onComplete() {
    }
}