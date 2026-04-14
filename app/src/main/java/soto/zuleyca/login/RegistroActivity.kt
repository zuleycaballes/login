package soto.zuleyca.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import soto.zuleyca.login.ui.theme.LoginTheme
import java.util.Calendar

class RegistroActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        database = Firebase.database.reference
        setContent {
            LoginTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Registrarse(
                        auth,
                        database,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        auth = Firebase.auth
    }
}

@Composable
fun Registrarse(auth:FirebaseAuth, database: DatabaseReference, modifier: Modifier = Modifier) {

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    var nombreError by rememberSaveable { mutableStateOf(false) }
    var correoError by rememberSaveable { mutableStateOf(false) }
    var contraError by rememberSaveable { mutableStateOf(false) }
    var confirmarError by rememberSaveable { mutableStateOf(false) }
    var fechaError by rememberSaveable { mutableStateOf(false) }


    val context = LocalContext.current

    fun edadValida(fecha: String):Boolean {
        val partes = fecha.split("/")

        if (partes.size != 3) return false

        val dia = partes[0].toIntOrNull() ?: return false
        val mes = partes[1].toIntOrNull() ?: return false
        val anio = partes[2].toIntOrNull() ?: return false

        val hoy = Calendar.getInstance()

        val anioActual = hoy.get(Calendar.YEAR)
        val mesActual = hoy.get(Calendar.MONTH) + 1
        val diaActual = hoy.get(Calendar.DAY_OF_MONTH)

        var edad = anioActual - anio

        if (mesActual < mes || (mesActual == mes && diaActual < dia)) {
            edad -= 1
        }

        return edad >= 18
    }

    fun calcularEdad(fecha: String): Int {
        val partes = fecha.split("/")

        val dia = partes[0].toInt()
        val mes = partes[1].toInt()
        val anio = partes[2].toInt()

        val hoy = Calendar.getInstance()

        val anioActual = hoy.get(Calendar.YEAR)
        val mesActual = hoy.get(Calendar.MONTH) + 1
        val diaActual = hoy.get(Calendar.DAY_OF_MONTH)

        var edad = anioActual - anio

        if (mesActual < mes || (mesActual == mes && diaActual < dia)) {
            edad = edad - 1
        }

        return edad
    }

    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text =  "Registro", fontSize =  24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                nombreError = it.isEmpty() },
            label = { Text(text = "Nombre completo")},
            isError = nombreError,
            supportingText = {
                if (nombreError) Text("Campo requerido")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                correoError = it.isEmpty() },
            label = { Text(text = "Correo electrónico")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = correoError,
            supportingText = {
                if (correoError) {
                    Text("Campo requerido")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contra,
            onValueChange = {
                contra = it
                contraError = it.isEmpty() },
            label = { Text(text = "Contraseña")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = contraError,
            supportingText = {
                if (contraError) Text("Campo requerido")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmar,
            onValueChange = {
                confirmar = it
                confirmarError = it.isEmpty() },
            label = { Text(text = "Confirmar contraseña")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmarError,
            supportingText = {
                if (confirmarError) Text("Campo requerido")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fecha,
            onValueChange = {
                fecha = it
                fechaError = it.isEmpty() },
            label = { Text(text = "Fecha de nacimiento (dd/mm/aaaa)")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = fechaError,
            supportingText = {
                if (fechaError) Text("Campo requerido")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            nombreError = nombre.isEmpty()
            correoError = correo.isEmpty()
            contraError = contra.isEmpty()
            confirmarError = confirmar.isEmpty() || contra != confirmar
            fechaError = fecha.isEmpty()

            if (nombreError || correoError || contraError || confirmarError || fechaError) {
                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@Button
            }

            if (!edadValida(fecha)) {
                Toast.makeText(context, "Debes ser mayor de 18 años", Toast.LENGTH_SHORT).show()
                return@Button
            }

            auth.createUserWithEmailAndPassword(correo, contra)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userID = auth.currentUser?.uid ?: "anonimo"
                        val edadCalculada = calcularEdad(fecha)

                        val usuario = Usuario(nombre, correo, fecha, edadCalculada.toString())

                        database.child("usuarios").child(userID).setValue(usuario)

                        Toast.makeText(context, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, PrincipalActivity::class.java)
                        intent.putExtra("usuario", nombre)
                        intent.putExtra("correo", correo)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                    }
                }
        }) { Text(text = "Registrarse")}

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick ={
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Volver al inicio")
        }
    }
}