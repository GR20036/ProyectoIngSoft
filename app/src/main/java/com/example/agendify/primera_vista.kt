package com.example.agendify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class primera_vista : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_primera_vista)

        auth = FirebaseAuth.getInstance()

        // Verifica si hay un usuario logueado
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Si hay un usuario autenticado, redirigir al Dashboard o pantalla principal
            val intent = Intent(this, Dashboard_Negocio::class.java)
            startActivity(intent)
            finish()
        } else {
            // Botón de registro de usuario
            val btnUsuario: Button = findViewById(R.id.btnUsuario)
            btnUsuario.setOnClickListener {
                // Lógica para llevar al registro de usuario
                val intent = Intent(this, Registro_Usuario::class.java)
                startActivity(intent)
            }

            // Botón de registro de negocio
            val btnNegocio: Button = findViewById(R.id.btnNegocio)
            btnNegocio.setOnClickListener {
                // Lógica para llevar al registro de negocio
                val intent = Intent(this, Registro_Negocio::class.java)
                startActivity(intent)
            }

            val textViewYaTienesCuenta = findViewById<TextView>(R.id.textViewYaTienesCuenta)

            textViewYaTienesCuenta.setOnClickListener {
                showLoginDialog()
            }
        }
    }

    private fun showLoginDialog() {
        // Construir el diálogo de alerta
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Iniciar sesión como")
        builder.setMessage("Selecciona si deseas iniciar sesión como usuario o como negocio")

        // Opción de iniciar sesión como usuario
        builder.setPositiveButton("Usuario") { dialog, _ ->
            // Abrir la actividad de login de usuario
            val intent = Intent(this, Login_Usuario::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        // Opción de iniciar sesión como negocio
        builder.setNegativeButton("Negocio") { dialog, _ ->
            // Abrir la actividad de login de negocio
            val intent = Intent(this, Login_Negocio::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        // Crear y mostrar el diálogo
        val dialog = builder.create()
        dialog.show()
    }
}