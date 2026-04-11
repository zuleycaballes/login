package soto.zuleyca.login

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Usuario(var name: String, var correo: String, var fechaNacimiento: String, var edad: String)