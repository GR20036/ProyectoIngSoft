package com.example.agendify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class primera_vista : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_primera_vista)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Botón de registro de usuario
        val btnUsuario: Button = findViewById(R.id.btnUsuario)
        btnUsuario.setOnClickListener {
            val intent = Intent(this, Registro_Usuario::class.java)
            startActivity(intent)
        }

        // Botón de registro de negocio
        val btnNegocio: Button = findViewById(R.id.btnNegocio)
        btnNegocio.setOnClickListener {
            val intent = Intent(this, Registro_Negocio::class.java)
            startActivity(intent)
        }

        val textViewYaTienesCuenta = findViewById<TextView>(R.id.textViewYaTienesCuenta)
        textViewYaTienesCuenta.setOnClickListener {
            showLoginDialog()
        }

        // Verifica si hay un usuario logueado
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Si hay un usuario autenticado, obtenemos el tipo de usuario desde Firestore
            checkUserTypeAndRedirect(currentUser.uid)
        }
    }

    // Verifica el tipo de usuario en Firestore y redirige a la vista correspondiente
    private fun checkUserTypeAndRedirect(userId: String) {
        val userRef = db.collection("users").document(userId)

        userRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val userType = document.getString("userType")
                if (userType == "Businesses") {
                    // Redirigir a la vista del negocio
                    val intent = Intent(this, Dashboard_Negocio::class.java)
                    startActivity(intent)
                    finish()
                } else if (userType == "User") {
                    // Redirigir a la vista del usuario
                    val intent = Intent(this, Dashboard_Usuario::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                // Si el documento no existe, mostrar algún mensaje de error
                // Aquí podrías mostrar un mensaje de error o manejar la excepción
            }
        }.addOnFailureListener { exception ->
            // Manejar el error en caso de que no se pueda obtener el documento
            // Podrías mostrar un mensaje de error si falla la conexión con Firestore
        }
    }

    private fun showLoginDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Iniciar sesión como")
        builder.setMessage("Selecciona si deseas iniciar sesión como usuario o como negocio")

        builder.setPositiveButton("Usuario") { dialog, _ ->
            val intent = Intent(this, Login_Usuario::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        builder.setNegativeButton("Negocio") { dialog, _ ->
            val intent = Intent(this, Login_Negocio::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}
