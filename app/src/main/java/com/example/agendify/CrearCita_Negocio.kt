package com.example.agendify

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.DateTime
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CrearCita_Negocio : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var businessId: String
    private lateinit var nombreCliente: EditText
    private lateinit var btnFechaCita: Button
    private lateinit var horaCita: Spinner
    private lateinit var servicioARealizar: Spinner
    private lateinit var btnGuardarCita: Button
    private lateinit var db: FirebaseFirestore
    private var selectedDate: Calendar? = null
    private var horaInicio: String? = null
    private var horaFin: String? = null
    // Lista que almacenará los días en los que el negocio trabaja
    private val diasDeTrabajo = mutableListOf<String>()

    // Mapa que almacenará los horarios de trabajo por cada día
    private val horarioDeTrabajo = mutableMapOf<String, Pair<String, String>>()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cita_negocio)

        db = FirebaseFirestore.getInstance()

        // Obtener businessId desde las SharedPreferences
        val sharedPreferences = getSharedPreferences("BusinessPrefs", MODE_PRIVATE)
        businessId = sharedPreferences.getString("businessId", "") ?: ""

        // Configurar el menú lateral
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
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

        val headerView = navigationView.getHeaderView(0)
        val navHeaderName: TextView = headerView.findViewById(R.id.tvNombreNegocio)
        val navHeaderLogo: ImageView = headerView.findViewById(R.id.imgLogoNegocio)

        val businessName = sharedPreferences.getString("businessName", "Nombre del Negocio")
        val businessLogo = sharedPreferences.getString("businessLogo", null)

        navHeaderName.text = businessName
        if (businessLogo != null) {
            Picasso.get().load(businessLogo).into(navHeaderLogo)
        }

        // Inicializar campos de la cita
        nombreCliente = findViewById(R.id.etNombreCliente)
        servicioARealizar = findViewById(R.id.spinnerServicio)
        btnFechaCita = findViewById(R.id.btnFechaCita)
        horaCita = findViewById(R.id.spinnerHora)
        btnGuardarCita = findViewById(R.id.btnGuardarCita)

        obtenerDatosNegocio()
        // Configurar adaptadores de los spinners
        configurarSpinners()

        // Configurar el calendario para seleccionar fechas válidas
        btnFechaCita.setOnClickListener {
            mostrarDatePicker()
        }

        btnGuardarCita.setOnClickListener {
            guardarCita()
        }
    }

    private fun configurarSpinners() {
        // Configuración del spinner de servicio
        val servicios = arrayOf("Corte de cabello", "Tinte", "Manicure", "Pedicure") // Ejemplo
        val adapterServicios = ArrayAdapter(this, android.R.layout.simple_spinner_item, servicios)
        adapterServicios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        servicioARealizar.adapter = adapterServicios
    }

    private fun guardarCita() {
        val nombre = nombreCliente.text.toString().trim()
        val servicio = servicioARealizar.selectedItem.toString()
        val hora = horaCita.selectedItem.toString()

        if (nombre.isEmpty() || servicio.isEmpty() || selectedDate == null || hora.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear la cita
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val fecha = sdf.format(selectedDate?.time)

        // Validar que no se haya superado el límite de citas a la misma hora
        citasEnMismaHora(fecha, hora) { count ->
            if (count >= obtenerLimiteCitasPorHora()) {
                Toast.makeText(this, "Ya se alcanzó el límite de citas para esta hora", Toast.LENGTH_SHORT).show()
            } else {
                // Crear la cita
                val cita = hashMapOf(
                    "cliente" to nombre,
                    "servicio" to servicio,
                    "fecha" to fecha,
                    "hora" to hora,
                    "businessId" to businessId,
                )

                    db.collection("citas_"+businessId)
                    .add(cita)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Cita creada correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al crear cita: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun citasEnMismaHora(fecha: String, hora: String, callback: (Int) -> Unit) {
        db.collection("citas_"+businessId)
            .whereEqualTo("fecha", fecha)
            .whereEqualTo("hora", hora)
            .get()
            .addOnSuccessListener { documents ->
                val count = documents.size()
                callback(count)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al consultar citas: ${exception.message}", Toast.LENGTH_SHORT).show()
                callback(0) // En caso de error, devolver 0 citas
            }
    }


    private fun obtenerLimiteCitasPorHora(): Int {
        val sharedPreferences = getSharedPreferences("BusinessPrefs", MODE_PRIVATE)
        return sharedPreferences.getInt("limiteCitasPorHora", 1) // Valor predeterminado 1
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
                val intent = Intent(this, GestionCitas::class.java)
                startActivity(intent)
            }
            R.id.nav_clientes -> {
                val intent = Intent(this, GestionCitas::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                cerrarSesion()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        val sharedPref = getSharedPreferences("BusinessPrefs", MODE_PRIVATE)
        sharedPref.edit().clear().apply()
        val intent = Intent(this, Login_Negocio::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun mostrarDatePicker() {
        val calendar = Calendar.getInstance()

        // Inicializar el DatePickerDialog con un solo OnDateSetListener
        val datePickerDialog = DatePickerDialog(
            this,
            null,  // Inicialmente sin listener
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Limitar el DatePickerDialog a fechas a partir del día actual
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        // Configurar el OnDateSetListener
        datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }

            if (esDiaDeTrabajo(selectedCalendar)) {
                selectedDate = selectedCalendar
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                btnFechaCita.text = sdf.format(selectedDate?.time)

                configurarSpinnerHoras(btnFechaCita.text.toString())
            } else {
                Toast.makeText(this, "El negocio no trabaja en esta fecha", Toast.LENGTH_SHORT).show()
            }
        }

        datePickerDialog.show()
    }


    private fun esDiaDeTrabajo(calendar: Calendar): Boolean {
        // Verifica si el día seleccionado está dentro de los días laborales
        val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "LUNES"
            Calendar.TUESDAY -> "MARTES"
            Calendar.WEDNESDAY -> "MIERCOLES"
            Calendar.THURSDAY -> "JUEVES"
            Calendar.FRIDAY -> "VIERNES"
            Calendar.SATURDAY -> "SABADO"
            Calendar.SUNDAY -> "DOMINGO"
            else -> ""
        }
        return diasDeTrabajo.contains(dayOfWeek)
    }

    private fun obtenerDatosNegocio() {
        // Supongamos que tienes el businessId previamente guardado
        val businessRef = db.collection("Businesses").document(businessId)

        businessRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val horario = document.get("horario") as Map<String, Map<String, String>>?

                if (horario != null) {
                    // Procesar los días y horas laborales
                    for ((dia, horas) in horario) {
                        val horaInicio = horas["inicio"]
                        val horaFin = horas["fin"]

                        if (horaInicio != null && horaFin != null) {
                            // Guardamos el horario para cada día
                            diasDeTrabajo.add(dia.uppercase())  // "lunes" -> "LUNES"
                            horarioDeTrabajo[dia.uppercase()] = Pair(horaInicio, horaFin)
                        }
                    }
                } else {
                    Toast.makeText(this, businessId, Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Error al obtener datos del negocio: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurarSpinnerHoras(fecha: String) {
        if (selectedDate != null) {
            // Obtener el día de la semana de la fecha seleccionada
            val dayOfWeek = when (selectedDate?.get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY -> "LUNES"
                Calendar.TUESDAY -> "MARTES"
                Calendar.WEDNESDAY -> "MIERCOLES"
                Calendar.THURSDAY -> "JUEVES"
                Calendar.FRIDAY -> "VIERNES"
                Calendar.SATURDAY -> "SABADO"
                Calendar.SUNDAY -> "DOMINGO"
                else -> null
            }

            if (dayOfWeek != null && horarioDeTrabajo.containsKey(dayOfWeek)) {
                // Obtener las horas de inicio y fin para el día seleccionado
                val (horaInicio, horaFin) = horarioDeTrabajo[dayOfWeek]!!

                generarHorasDisponiblesConCupo(horaInicio, horaFin, fecha, obtenerLimiteCitasPorHora()) { horasDisponibles ->
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, horasDisponibles)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    horaCita.adapter = adapter

                    if (horasDisponibles.isEmpty()) {
                        Toast.makeText(this, "No hay horarios disponibles para esta fecha.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "El negocio no trabaja el día seleccionado", Toast.LENGTH_SHORT).show()
                horaCita.adapter = null  // Limpiar el Spinner si el día no es laboral
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun generarHorasDisponiblesConCupo(horaInicio: String, horaFin: String, fecha: String, limiteCitasPorHora: Int, callback: (List<String>) -> Unit): List<String> {
        // Cambiar a formato de 12 horas con AM/PM
        val formatoHora = SimpleDateFormat("hh:mm a", Locale.US) // Formato 12 horas con AM/PM
        val listaHoras = mutableListOf<String>()

        try {
            val inicio = formatoHora.parse(horaInicio)
            val fin = formatoHora.parse(horaFin)

            val calendar = Calendar.getInstance()
            calendar.time = inicio

            // Iterar sobre las horas en intervalos de 30 minutos
            while (calendar.time.before(fin)) {
                val horaActual = formatoHora.format(calendar.time)

                // Verificar cuántas citas existen en esta fecha y hora antes de agregarla
                citasEnMismaHora(fecha, horaActual) { count ->
                    if (count < limiteCitasPorHora) {
                        listaHoras.add(horaActual) // Solo agregar si no se ha alcanzado el límite
                    }

                    // Si se han verificado todas las horas, devolver la lista
                    if (calendar.time == fin) {
                        listaHoras.sortWith(compareBy { formatoHora.parse(it) })
                        callback(listaHoras)
                    }
                }

                // Avanzar 30 minutos
                calendar.add(Calendar.MINUTE, 30)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al generar horas: ${e.message}", Toast.LENGTH_SHORT).show()
            callback(emptyList()) // En caso de error, devolver una lista vacía
        }
        return listaHoras
    }
}
