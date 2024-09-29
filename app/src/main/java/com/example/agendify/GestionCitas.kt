package com.example.agendify

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class GestionCitas : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var citasAdapter: CitasAdapter
    private val citasList = mutableListOf<Cita>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_citas)

        val sharedPreferences = getSharedPreferences("BusinessPrefs", MODE_PRIVATE)
        val businessName = sharedPreferences.getString("businessName", "Nombre del Negocio")
        val businessLogo = sharedPreferences.getString("businessLogo", null)

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

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

// Obtener la vista del encabezado del NavigationView
        val headerView = navigationView.getHeaderView(0) // Obtiene el primer (y único) encabezado

// Acceder a los elementos del encabezado
        val navHeaderName: TextView = headerView.findViewById(R.id.tvNombreNegocio)
        val navHeaderLogo: ImageView = headerView.findViewById(R.id.imgLogoNegocio)

        navHeaderName.text = businessName
        if (businessLogo != null) {
            Picasso.get().load(businessLogo).into(navHeaderLogo)
        }

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
        if (businessId != null) {
            FirebaseFirestore.getInstance().collection("citas_"+businessId)
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

    private fun cerrarSesion() {
        // Cierra sesión en Firebase
        FirebaseAuth.getInstance().signOut()

        // Limpia cualquier dato en SharedPreferences si es necesario
        val sharedPref = getSharedPreferences("BusinessPrefs", MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Redirige al usuario a la pantalla de login
        val intent = Intent(this, Login_Negocio::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_dashboard -> {
                val intent = Intent(this, Dashboard_Negocio::class.java)
                startActivity(intent)
            }
            R.id.nav_citas -> {
                val intent = Intent(this, CrearCita_Negocio::class.java)
                startActivity(intent)
            }
            R.id.nav_servicios -> {
                val intent = Intent(this, Login_Usuario::class.java)
                startActivity(intent)
            }
            R.id.nav_clientes -> {
                val intent = Intent(this, Login_Usuario::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                cerrarSesion()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        citasAdapter.notifyDataSetChanged()
        super.onResume()
    }
}
