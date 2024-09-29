package com.example.agendify

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login_Negocio : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var menuButton: ImageButton
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_negocio)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Enlazar vistas con los elementos de la interfaz
        emailEditText = findViewById(R.id.emailNegocioEditText)
        passwordEditText = findViewById(R.id.passwordNegocioEditText)
        loginButton = findViewById(R.id.loginNegocioButton)
        menuButton = findViewById(R.id.menuButton)

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
                        // Acción para registro de usuario normal
                        val intent = Intent(this, Registro_Usuario::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
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
                        // Acción para login de negocio (actual)
                        true
                    }

                    else -> false
                }
            }

            // Mostrar el menú emergente
            popup.show()
        }

        // Configurar el listener para el botón de inicio de sesión
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Iniciar sesión con Firebase Auth
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Obtener el negocio con el email de login
                            db.collection("Businesses")
                                .whereEqualTo("email", email)
                                .get()
                                .addOnSuccessListener { documents ->
                                    if (!documents.isEmpty) {
                                        val business = documents.first()
                                        val businessId = business.id
                                        val businessName =  business.getString("nombre_negocio")
                                        val businessLogoUri = business.getString("logoUri")
                                        val citasSimultaneas = business.getString("num_citas")

                                        // Guardar el businessId en SharedPreferences
                                        val sharedPreferences = getSharedPreferences("BusinessPrefs", MODE_PRIVATE)
                                        val editor = sharedPreferences.edit()
                                        editor.putString("businessId", businessId)
                                        editor.putString("businessName", businessName)
                                        editor.putString("businessLogo", businessLogoUri)
                                        if (citasSimultaneas != null) {
                                            editor.putInt("limiteCitasPorHora", citasSimultaneas.toInt())
                                        }
                                        editor.apply()

                                        // Mostrar mensaje de éxito y abrir Dashboard
                                        Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, Dashboard_Negocio::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Negocio no encontrado", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error al buscar el negocio", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Error de autenticación", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
