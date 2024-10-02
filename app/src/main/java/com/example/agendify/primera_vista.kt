package com.example.agendify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
        // Verifica si hay un usuario logueado
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Si hay un usuario autenticado, obtenemos el tipo de usuario desde Firestore
            checkUserTypeAndRedirect(currentUser.uid)
        }

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
    }

    // Verifica el tipo de usuario en Firestore y redirige a la vista correspondiente
    private fun checkUserTypeAndRedirect(userId: String) {
        val userRef = db.collection("Users").whereEqualTo("userId", userId)

        userRef.get().addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                // Si el usuario existe en la colección "Users", redirigir a Dashboard_Usuario
                val intent = Intent(this, Dashboard_Usuario::class.java)
                startActivity(intent)
                finish()
            } else {
                // Si no está en "Users", buscar en "Businesses"
                val negocioRef = db.collection("Businesses").whereEqualTo("userId", userId)

                negocioRef.get().addOnSuccessListener { businessDocs ->
                    if (!businessDocs.isEmpty) {
                        // Si el negocio existe, redirigir a Dashboard_Negocio
                        val intent = Intent(this, Dashboard_Negocio::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Si no se encuentra ni como usuario ni como negocio, puedes agregar un mensaje o quedarte en la vista actual
                        Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "Error al obtener los datos del negocio: $exception", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error al obtener los datos del usuario: $exception", Toast.LENGTH_SHORT).show()
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
