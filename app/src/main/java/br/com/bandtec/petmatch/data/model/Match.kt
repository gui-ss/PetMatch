package br.com.bandtec.petmatch.data.model

import com.google.gson.annotations.SerializedName

data class Match(
    @SerializedName("id") var id: Int,

    @SerializedName("idDoador") var idDoador: Int,

    @SerializedName("idAdotante") var idAdotante: Int,

    @SerializedName("idPet") var idPet: Int,

    @SerializedName("adotanteGostou") var adotanteGostou: String,

    @SerializedName("doadorGostou") var doadorGostou: String,

    @SerializedName("nomeAdotante") var nomeAdotante: String,

    @SerializedName("emailAdotante") var emailAdotante: String,

    @SerializedName("telefoneAdotante") var telefoneAdotante: String,

    @SerializedName("fotoAdotante") var fotoAdotante: String,

    @SerializedName("nomePet") var nomePet: String,

    @SerializedName("fotoPet") var fotoPet: String,

    @SerializedName("nomeDoador") var nomeDoador: String,

    @SerializedName("emailDoador") var emailDoador: String,

    @SerializedName("telefoneDoador") var telefoneDoador: String,

    @SerializedName("fotoDoador") var fotoDoador: String,

    @SerializedName("QuerPetWek") var QuerPetWek: String,
)
