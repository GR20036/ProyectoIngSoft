package com.example.agendify

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Login_Usuario : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var menuButton: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_usuario)

        auth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.emailUsuarioEditText)
        passwordEditText = findViewById(R.id.passwordUsuarioEditText)
        loginButton = findViewById(R.id.loginUsuarioButton)
        menuButton = findViewById(R.id.menuButton2)

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
                        // Acción para login de negocio (actual)
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

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Redirigir a la pantalla principal del usuario
                            val intent = Intent(this, Dashboard_Usuario::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)

                            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error en inicio de sesión", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor completa los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}