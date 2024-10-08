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

class GestionServicios : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var serviciosAdapter: ServiciosAdapter
    private val serviciosList = mutableListOf<Servicio>()

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_servicios)

        val sharedPreferences = getSharedPreferences("BusinessPrefs", MODE_PRIVATE)
        val businessName = sharedPreferences.getString("businessName", "Nombre del Negocio")
        val businessLogo = sharedPreferences.getString("businessLogo", null)
        val btn_nuevo_servicio: FloatingActionButton = findViewById(R.id.btn_nuevo_servicio_negocio)

        // Configurar Toolbar y DrawerLayout
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout3)

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

        serviciosAdapter = ServiciosAdapter(serviciosList)

        val recyclerViewServicios: RecyclerView = findViewById(R.id.recyclerViewServicios)
        recyclerViewServicios.layoutManager = LinearLayoutManager(this)
        recyclerViewServicios.adapter = serviciosAdapter


        btn_nuevo_servicio.setOnClickListener {
            val dialog = Agregar_Servicio()
            dialog.setOnSaveListener { nombre, descripcion, duracion, precio ->
                val sharedPreferences = getSharedPreferences("BusinessPrefs", MODE_PRIVATE)
                val businessId = sharedPreferences.getString("businessId", null)

                // Crear objeto servicio
                val servicio = Servicio(nombre, descripcion, duracion, precio)

                // Añadir a la lista y notificar al adapter
                serviciosList.add(servicio)
                serviciosAdapter.notifyDataSetChanged()

                // Guardar en Firebase Firestore si es necesario
                if (businessId != null) {
                    FirebaseFirestore.getInstance().collection("Businesses")
                        .document(businessId)
                        .collection("servicios")
                        .add(servicio)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Servicio agregado", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al agregar servicio", Toast.LENGTH_SHORT).show()
                        }
                }
            }
                dialog.show(supportFragmentManager, "AddServiceDialog")
            }

        // Cargar servicios desde Firebase
        cargarServicios()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cargarServicios() {
        val sharedPreferences = getSharedPreferences("BusinessPrefs", MODE_PRIVATE)
        val businessId = sharedPreferences.getString("businessId", null)
        if (businessId != null) {

            serviciosList.clear()

            FirebaseFirestore.getInstance().collection("Businesses").document(businessId).collection("servicios")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val servicio = document.toObject(Servicio::class.java)
                        serviciosList.add(servicio)
                    }
                    serviciosAdapter.notifyDataSetChanged()
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
                val intent = Intent(this, GestionCitas::class.java)
                startActivity(intent)
            }
            R.id.nav_servicios -> {

            }
            R.id.nav_logout -> {
                cerrarSesion()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}