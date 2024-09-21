package com.example.agendify

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.RecyclerView

class GestionCitas : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var citasAdapter: CitasAdapter
    private val citasList = mutableListOf<Cita>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_citas)

        // Configurar Toolbar y DrawerLayout
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout2)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open, R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Configurar RecyclerView
        citasAdapter = CitasAdapter(citasList) { cita ->
            // Al hacer clic en una cita, mostrar detalles
            val intent = Intent(this, DetallesCita::class.java)
            intent.putExtra("citaId", cita.id)
            startActivity(intent)
        }

        val recyclerViewCitas: RecyclerView = findViewById(R.id.recyclerViewCitas)
        recyclerViewCitas.layoutManager = LinearLayoutManager(this)
        recyclerViewCitas.adapter = citasAdapter

        // Cargar citas desde Firebase
        cargarCitas()
    }

    private fun cargarCitas() {
        // Suponiendo que tienes el businessId guardado
        val sharedPreferences = getSharedPreferences("BusinessPrefs", MODE_PRIVATE)
        val businessId = sharedPreferences.getString("businessId", null)

        Toast.makeText(this,businessId.toString(), Toast.LENGTH_SHORT).show()


        if (businessId != null) {
            FirebaseFirestore.getInstance().collection("citas")
                .whereEqualTo("businessId", businessId)
                .get()
                .addOnSuccessListener { documents ->
                    citasList.clear()
                    for (document in documents) {
                        var cita = document.toObject(Cita::class.java)
                        cita.id = document.id
                        citasList.add(cita)
                    }
                    citasAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                }

        }
    }
}
