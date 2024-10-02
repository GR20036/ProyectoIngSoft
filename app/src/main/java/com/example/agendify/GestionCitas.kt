package com.example.agendify

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
        val btn_nueva_cita: FloatingActionButton = findViewById(R.id.btn_nueva_cita_negocio)

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

        btn_nueva_cita.setOnClickListener {
            val intent = Intent(this, CrearCita_Negocio::class.java)
            startActivity(intent)
        }

        // Cargar citas desde Firebase
        cargarCitas()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cargarCitas() {
        val sharedPreferences = getSharedPreferences("BusinessPrefs", MODE_PRIVATE)
        val businessId = sharedPreferences.getString("businessId", null)
        if (businessId != null) {
            FirebaseFirestore.getInstance().collection("Businesses").document(businessId).collection("citas")
                .orderBy("fecha", Query.Direction.ASCENDING)  // Ordenar por fecha
                .orderBy("hora", Query.Direction.ASCENDING)    // Ordenar por hora
                .get()
                .addOnSuccessListener { documents ->

                    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.US)
                    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)
                    val today = Calendar.getInstance().time
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US) // Asegúrate que el formato coincida con tu formato de fecha
                    citasList.clear()
                    for (document in documents) {
                        val cita = document.toObject(Cita::class.java)
                        cita.id = document.id
                        // Convertir el String de fecha a un objeto Date
                        val citaFecha: Date? = dateFormat.parse(cita.fecha)
                        if (citaFecha != null && !citaFecha.before(today)) {
                            citasList.add(cita)
                        }
                    }
                    citasList.sortWith(compareBy(
                        { LocalDate.parse(it.fecha, dateFormatter) },  // Parsear fecha
                        { LocalTime.parse(it.hora, timeFormatter) }   // Parsear hora
                    ))
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

            }
            R.id.nav_servicios -> {
                val intent = Intent(this, GestionServicios::class.java)
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
        cargarCitas()
        super.onResume()
    }
}
