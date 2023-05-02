package br.com.bandtec.petmatch.data

import br.com.bandtec.petmatch.data.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRemoteDataSource {

    interface UserCallback{
        fun onSuccess(response: User?)

        fun onError(message: String?)

        fun onErrorLogin(code: Int?)

        fun onErrorRegister(code: Int?)

        fun onComplete()
    }

    fun findUser(callback: UserCallback, id: String){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .getUserMock(id = id)
            .enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful)
                        callback.onSuccess(response.body())

                    callback.onComplete()
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    callback.onError(t.message)
                    callback.onComplete()
                }

            })
    }


    fun loginUser(callback: UserCallback, email: String, senha: String){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .userLogin(email = email, senha = senha)
            .enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code() == 200){
                        callback.onSuccess(response.body())
                        callback.onComplete()

                    }else if(response.code() == 404){
                        callback.onErrorLogin(response.code())
                        callback.onComplete()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    callback.onError(t.message)
                    callback.onComplete()
                }

            })
    }

    fun addUser(callback: UserCallback, user: User){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .addUser(user)
            .enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code() == 200){
                        callback.onSuccess(response.body())
                        callback.onComplete()
                    }else{
                        callback.onErrorRegister(response.code())
                        callback.onComplete()

                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    callback.onErrorRegister(200)
                    callback.onComplete()
                }

            })
    }
}