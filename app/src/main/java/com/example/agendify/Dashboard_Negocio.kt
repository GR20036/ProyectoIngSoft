package com.example.agendify

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class Dashboard_Negocio : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var tvProximasCitasCount: TextView
    private lateinit var tvTotalCitas: TextView
    private lateinit var tvIngresosEstimados: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_negocio)

        val sharedPreferences = getSharedPreferences("BusinessPrefs", MODE_PRIVATE)
        val businessName = sharedPreferences.getString("businessName", "Nombre del Negocio")
        val businessLogo = sharedPreferences.getString("businessLogo", null)

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configurar el toggle para el DrawerLayout
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


        // Inicializar las vistas de texto para mostrar estadísticas
        tvProximasCitasCount = findViewById(R.id.tvProximasCitasCount)
        tvTotalCitas = findViewById(R.id.tvTotalCitas)
        tvIngresosEstimados = findViewById(R.id.tvIngresosEstimados)

        // Llamada para mostrar las estadísticas iniciales
        mostrarEstadisticas()
    }

    private fun mostrarEstadisticas() {
        // Mostrar estadísticas simuladas o reales aquí
        val citasPendientes = 5 // Simulado
        val totalCitas = 20 // Simulado
        val ingresosEstimados = 1500.00 // Simulado

        // Configurar el texto de las estadísticas
        tvProximasCitasCount.text = "$citasPendientes citas pendientes"
        tvTotalCitas.text = "Total de citas: $totalCitas"
        tvIngresosEstimados.text = "Ingresos estimados: $$ingresosEstimados"
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_dashboard -> {
                // Ya estamos en el Dashboard
            }
            R.id.nav_citas -> {
                val intent = Intent(this, GestionCitas::class.java)
                startActivity(intent)
            }
            R.id.nav_servicios -> {
                val intent = Intent(this, GestionServicios::class.java)
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


    override fun onBackPressed() {
        // Cerrar el Navigation Drawer si está abierto
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
}
