package com.example.agendify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Registro_Usuario : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var menuButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val userName = findViewById<EditText>(R.id.userName)
        val userEmail = findViewById<EditText>(R.id.userEmail)
        val userPassword = findViewById<EditText>(R.id.userPassword)
        val registerUserButton = findViewById<Button>(R.id.registerUserButton)

        registerUserButton.setOnClickListener {
            val name = userName.text.toString()
            val email = userEmail.text.toString()
            val password = userPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(name, email, password)
            } else {
                Toast.makeText(
                    this,
                    "Por favor llena todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        menuButton = findViewById(R.id.menuButton3)

        // Configurar el listener para el botón de menú
        menuButton.setOnClickListener {
            // Crear el PopupMenu y vincularlo con el botón
            val popup = PopupMenu(this, menuButton)
            // Inflar el menú desde un archivo XML
            popup.menuInflater.inflate(R.menu.menu_auth, popup.menu)

            // Establecer el listener de clic para manejar la selección de opción
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.registerUser_it -> {
                        // Acción para registro de usuario (actual)
                        true
                    }

                    R.id.registerBusiness_it -> {
                        // Acción para registro de negocio
                        val intent = Intent(this, Registro_Negocio::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        true
                    }

                    R.id.loginUser_it -> {
                        // Acción para login de usuario normal
                        val intent = Intent(this, Login_Usuario::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        true
                    }

                    R.id.loginBusiness_it -> {
                        // Acción para login de usuario negocio
                        val intent = Intent(this, Login_Negocio::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }

            // Mostrar el menú emergente
            popup.show()
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val user = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "userId" to userId
                    )

                    db.collection("Users").add(user)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Usuario registrado exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Error al registrar usuario: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Error de autenticación: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
