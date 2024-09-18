package com.example.agendify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class primera_vista : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_primera_vista)

        // Botón de registro de usuario
        val btnUsuario: Button = findViewById(R.id.btnUsuario)
        btnUsuario.setOnClickListener {
            // Lógica para llevar al registro de usuario
            val intent = Intent(this, Registro_Negocio::class.java)
            startActivity(intent)
        }

        // Botón de registro de negocio
        val btnNegocio: Button = findViewById(R.id.btnNegocio)
        btnNegocio.setOnClickListener {
            // Lógica para llevar al registro de negocio
            val intent = Intent(this, Registro_Negocio::class.java)
            startActivity(intent)
        }
    }
}