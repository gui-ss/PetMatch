package br.com.bandtec.petmatch.presentation

import br.com.bandtec.petmatch.activities.home.HomeActivity
import br.com.bandtec.petmatch.activities.signin.SignInActivity
import br.com.bandtec.petmatch.data.UserRemoteDataSource
import br.com.bandtec.petmatch.data.model.User

class UserPresenter(signInActivity: SignInActivity, data: UserRemoteDataSource): UserRemoteDataSource.UserCallback {

    var homeActivity: HomeActivity = HomeActivity()
    var view: SignInActivity = signInActivity
    var viewMain: HomeActivity = homeActivity
    var dataSource: UserRemoteDataSource = data

    fun findUserBy(id: String){
        this.dataSource.findUser(this, id)
    }

    fun userLogin(email: String, senha: String){
        this.dataSource.loginUser(this, email, senha)
    }

    override fun onSuccess(response: User?) {
        if (response != null){
            view.getIdUserLogged(response)
            view.userLogin(200)
        }
    }

    override fun onError(message: String?) {
       if (message != null){
           view.showFailure(message)
           view.showErrorLogin()
       }
    }

    override fun onErrorLogin(code: Int?) {
        view.userLogin(code)
    }

    override fun onErrorRegister(code: Int?) {
    }

    override fun onComplete() {
    }
}