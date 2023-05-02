package br.com.bandtec.petmatch.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id: Int?,

    @SerializedName("nome") var usuario_nome: String,

    @SerializedName("idade") var usuario_idade: String,

    @SerializedName("apelido") var usuario_apelido: String,

    @SerializedName("cep") var usuario_cep: String,

    @SerializedName("telefone") var usuario_telefone: String,

    @SerializedName("sexo") var usuario_sexo: String,

    @SerializedName("email") var usuario_email: String,

    @SerializedName("cpf") var usuario_cpf: String,

    @SerializedName("fotoPerfil") var usuario_foto_perfil: String,

    @SerializedName("senha") var usuario_senha: String

    )