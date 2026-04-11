package soto.zuleyca.login

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import soto.zuleyca.login.ui.theme.LoginTheme

class PrincipalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //var usuario = intent.getStringExtra("usuario") ?: "anonimo"

        var uid = Firebase.auth.currentUser?.uid ?: ""
        var myRef = Firebase.database.getReference("usuarios").child(uid)

        setContent {
            LoginTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaInicio(
                        myRef,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaInicio(myRef: DatabaseReference, modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("Cargando...") }
    var correo by remember { mutableStateOf("Cargando...") }
    var fechaNacimiento by remember { mutableStateOf("Cargando...") }
    var edad by remember { mutableStateOf("Cargando...") }

    val context = LocalContext.current

    myRef.get().addOnSuccessListener { snapshot ->
        nombre = snapshot.child("name").value.toString()
        correo = snapshot.child("correo").value.toString()
        fechaNacimiento = snapshot.child("fechaNacimiento").value.toString()
        edad = snapshot.child("edad").value.toString()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "BIENVENIDO", fontSize = 40.sp)
        Spacer(modifier = modifier.height(17.dp))
        Text(text = "$nombre", fontSize = 32.sp)
        Spacer(modifier = modifier.height(17.dp))
        Text(text = "$correo", fontSize = 32.sp)
        Spacer(modifier = modifier.height(17.dp))
        Text(text = "$fechaNacimiento", fontSize = 32.sp)
        Spacer(modifier = modifier.height(17.dp))
        Text(text = "$edad", fontSize = 32.sp)
        Spacer(modifier = modifier.height(17.dp))
        Button(onClick = {
            Firebase.auth.signOut()
            (context as? Activity)?.finish()
        }) {
            Text(text = "Cerrar Sesion")
        }
    }
}
