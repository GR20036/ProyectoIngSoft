package com.example.agendify

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*


data class Horario(
    val inicio: String = "",
    val fin: String = ""
)

class Registro_Negocio : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private val REQUEST_CODE_SCHEDULE = 1
    private var logoUri: Uri? = null
    private lateinit var menuButton: ImageButton

    // Variables para almacenar los horarios de trabajo
    private val horariosLaborales = mutableMapOf<String, Horario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_negocio)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        val businessName = findViewById<EditText>(R.id.businessName)
        val spinnerCategoria = findViewById<Spinner>(R.id.businessCategory)
        val businessAddress = findViewById<EditText>(R.id.businessAddress)
        val businessPhone = findViewById<EditText>(R.id.businessPhone)
        val businessEmail = findViewById<EditText>(R.id.businessEmail)
        val businessPassword = findViewById<EditText>(R.id.businessPassword)
        val uploadLogoButton = findViewById<Button>(R.id.uploadLogoButton)
        val registerBusinessButton = findViewById<Button>(R.id.registerBusinessButton)
        val businessHorario = findViewById<Button>(R.id.editHorarioRegistro)
        val categorias = arrayOf("Salud", "Belleza", "Deportes", "Restaurantes", "Tecnología")

        businessHorario.setOnClickListener {
            val intent = Intent(this, configuracion_horario::class.java)

            // Pasar los horarios actuales al intent
            horariosLaborales.forEach { (dia, horario) ->
                if (horario.inicio.isNotEmpty() && horario.fin.isNotEmpty()) {
                    intent.putExtra("${dia}_inicio", horario.inicio)
                    intent.putExtra("${dia}_fin", horario.fin)
                }
            }

            startActivityForResult(intent, REQUEST_CODE_SCHEDULE)
        }
        menuButton = findViewById(R.id.menuButton4)

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
                        // Acción para registro de negocio (actual)
                        true
                    }

                    R.id.loginUser_it -> {

                        // Acción para login de usuario normal
                        val intent = Intent(this, Login_Usuario::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
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

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapter

        // Botón para subir logo
        uploadLogoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        // Botón para registrar el negocio
        registerBusinessButton.setOnClickListener {
            val name = businessName.text.toString()
            val category = spinnerCategoria.selectedItem.toString()
            val address = businessAddress.text.toString()
            val phone = businessPhone.text.toString()
            val email = businessEmail.text.toString()
            val password = businessPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerBusiness(name, category, address, phone, email, password)
            } else {
                Toast.makeText(
                    this,
                    "Por favor llena todos los campos obligatorios",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Método combinado para manejar los resultados de actividades
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Manejo del resultado de la selección de horario
        if (requestCode == REQUEST_CODE_SCHEDULE && resultCode == RESULT_OK) {
            // Recuperar los horarios seleccionados para cada día
            val horarioLunesInicio = data?.getStringExtra("lunes_inicio")
            val horarioLunesFin = data?.getStringExtra("lunes_fin")
            val horarioMartesInicio = data?.getStringExtra("martes_inicio")
            val horarioMartesFin = data?.getStringExtra("martes_fin")
            val horarioMiercolesInicio = data?.getStringExtra("miercoles_inicio")
            val horarioMiercolesFin = data?.getStringExtra("miercoles_fin")
            val horarioJuevesInicio = data?.getStringExtra("jueves_inicio")
            val horarioJuevesFin = data?.getStringExtra("jueves_fin")
            val horarioViernesInicio = data?.getStringExtra("viernes_inicio")
            val horarioViernesFin = data?.getStringExtra("viernes_fin")
            val horarioSabadoInicio = data?.getStringExtra("sabado_inicio")
            val horarioSabadoFin = data?.getStringExtra("sabado_fin")
            val horarioDomingoInicio = data?.getStringExtra("domingo_inicio")
            val horarioDomingoFin = data?.getStringExtra("domingo_fin")

            if (horarioLunesInicio != null && horarioLunesFin != null) {
                horariosLaborales["lunes"] = Horario(horarioLunesInicio, horarioLunesFin)
            }
            if (horarioMartesInicio != null && horarioMartesFin != null) {
                horariosLaborales["martes"] = Horario(horarioMartesInicio, horarioMartesFin)
            }
            if (horarioMiercolesInicio != null && horarioMiercolesFin != null) {
                horariosLaborales["miercoles"] = Horario(horarioMiercolesInicio, horarioMiercolesFin)
            }
            if (horarioJuevesInicio != null && horarioJuevesFin != null) {
                horariosLaborales["jueves"] = Horario(horarioJuevesInicio, horarioJuevesFin)
            }
            if (horarioViernesInicio != null && horarioViernesFin != null) {
                horariosLaborales["viernes"] = Horario(horarioViernesInicio, horarioViernesFin)
            }
            if (horarioSabadoInicio != null && horarioSabadoFin != null) {
                horariosLaborales["sabado"] = Horario(horarioSabadoInicio, horarioSabadoFin)
            }
            if (horarioDomingoInicio != null && horarioDomingoFin != null) {
                horariosLaborales["domingo"] = Horario(horarioDomingoInicio, horarioDomingoFin)
            }

            // Actualizar el texto en la UI para mostrar los horarios seleccionados
            val horariosText = StringBuilder()
            if (horarioLunesInicio != null && horarioLunesFin != null) {
                horariosText.append("Lunes: $horarioLunesInicio - $horarioLunesFin\n")
            }
            if (horarioMartesInicio != null && horarioMartesFin != null) {
                horariosText.append("Martes: $horarioMartesInicio - $horarioMartesFin\n")
            }
            if (horarioMiercolesInicio != null && horarioMiercolesFin != null) {
                horariosText.append("Miercoles: $horarioMiercolesInicio - $horarioMiercolesFin\n")
            }
            if (horarioJuevesInicio != null && horarioJuevesFin != null) {
                horariosText.append("Jueves: $horarioJuevesInicio - $horarioJuevesFin\n")
            }
            if (horarioViernesInicio != null && horarioViernesFin != null) {
                horariosText.append("Viernes: $horarioViernesInicio - $horarioViernesFin\n")
            }
            if (horarioSabadoInicio != null && horarioSabadoFin != null) {
                horariosText.append("Sabado: $horarioSabadoInicio - $horarioSabadoFin\n")
            }
            if (horarioDomingoInicio != null && horarioDomingoFin != null) {
                horariosText.append("Domingo: $horarioDomingoInicio - $horarioDomingoFin\n")
            }

            val businessHorarioText = findViewById<TextView>(R.id.txtHorario)
            businessHorarioText.text = "El horario del negocio es:\n$horariosText"
        }

        // Manejo del resultado de la selección de imagen
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            logoUri = data?.data
            Toast.makeText(this, "Logo seleccionado", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para registrar el negocio
    private fun registerBusiness(
        name: String,
        category: String,
        address: String,
        phone: String,
        email: String,
        password: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val business = hashMapOf(
                        "nombre_negocio" to name,
                        "categoria" to category,
                        "direccion" to address,
                        "telefono" to phone,
                        "email" to email,
                        "userId" to userId,
                        "horario" to horariosLaborales // Guardar horarios laborales
                    )

                    // Guardar datos en Firestore
                    db.collection("Businesses").add(business)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Negocio registrado exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Subir logo a Firebase Storage
                            logoUri?.let { uri ->
                                val storageRef =
                                    storage.reference.child("business_logos/${userId}.jpg")
                                storageRef.putFile(uri)
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Error al registrar el negocio: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Error de autenticación: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}

