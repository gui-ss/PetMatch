package br.com.bandtec.petmatch.data.model

import com.google.gson.annotations.SerializedName

data class Like(
    @SerializedName("idPet") var idPet: Int,

    @SerializedName("adotanteGostou") var adotanteGostou: Boolean
)
