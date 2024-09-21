package com.example.agendify

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class DetallesCita : AppCompatActivity() {

    private lateinit var tvClienteDetalle: TextView
    private lateinit var tvServicioDetalle: TextView
    private lateinit var tvFechaDetalle: TextView
    private lateinit var tvHoraDetalle: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_cita)

        // Inicializar las vistas de texto
        tvClienteDetalle = findViewById(R.id.tvClienteDetalle)
        tvServicioDetalle = findViewById(R.id.tvServicioDetalle)
        tvFechaDetalle = findViewById(R.id.tvFechaDetalle)
        tvHoraDetalle = findViewById(R.id.tvHoraDetalle)

        val citaId = intent.getStringExtra("citaId")

        if (citaId != null) {
            cargarDetallesCita(citaId)
        }
    }

    private fun cargarDetallesCita(citaId: String) {
        FirebaseFirestore.getInstance().collection("citas")
            .document(citaId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val cita = document.toObject(Cita::class.java)
                    if (cita != null) {
                        tvClienteDetalle.text = cita.cliente
                        tvServicioDetalle.text = cita.servicio
                        tvFechaDetalle.text = cita.fecha
                        tvHoraDetalle.text = cita.hora
                    }
                }
            }
    }
}
