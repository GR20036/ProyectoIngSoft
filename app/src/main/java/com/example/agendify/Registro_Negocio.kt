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

class Registro_Negocio : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private var logoUri: Uri? = null

    // Variables para almacenar los horarios de trabajo
    private val horariosLaborales = mutableMapOf<String, Pair<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_negocio)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        val businessName = findViewById<EditText>(R.id.businessName)
        val businessCategory = findViewById<EditText>(R.id.businessCategory)
        val businessAddress = findViewById<EditText>(R.id.businessAddress)
        val businessPhone = findViewById<EditText>(R.id.businessPhone)
        val businessEmail = findViewById<EditText>(R.id.businessEmail)
        val businessUsername = findViewById<EditText>(R.id.businessUsername)
        val businessPassword = findViewById<EditText>(R.id.businessPassword)
        val uploadLogoButton = findViewById<Button>(R.id.uploadLogoButton)
        val registerBusinessButton = findViewById<Button>(R.id.registerBusinessButton)

        // Horarios laborales: lunes como ejemplo, puedes añadir más días
        val checkLunes = findViewById<CheckBox>(R.id.checkLunes)
        val btnHoraInicioLunes = findViewById<Button>(R.id.btnHoraInicioLunes)
        val btnHoraFinLunes = findViewById<Button>(R.id.btnHoraFinLunes)

        checkLunes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mostrarTimePickerAMPM { horaInicio, minutoInicio, amPmInicio ->
                    val horaFormateadaInicio = formatearHora(horaInicio, minutoInicio, amPmInicio)
                    btnHoraInicioLunes.text = horaFormateadaInicio
                    mostrarTimePickerAMPM { horaFin, minutoFin, amPmFin ->
                        val horaFormateadaFin = formatearHora(horaFin, minutoFin, amPmFin)
                        btnHoraFinLunes.text = horaFormateadaFin
                        horariosLaborales["Lunes"] = Pair(horaFormateadaInicio, horaFormateadaFin)
                    }
                }
            } else {
                horariosLaborales.remove("Lunes")
            }
        }

        // Botón para seleccionar ubicación en Google Maps
        val btnAgregarUbicacion = findViewById<Button>(R.id.addLocation)
        btnAgregarUbicacion.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=negocios+cercanos")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(this, "Google Maps no está instalado", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para subir logo
        uploadLogoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        // Botón para registrar el negocio
        registerBusinessButton.setOnClickListener {
            val name = businessName.text.toString()
            val category = businessCategory.text.toString()
            val address = businessAddress.text.toString()
            val phone = businessPhone.text.toString()
            val email = businessEmail.text.toString()
            val username = businessUsername.text.toString()
            val password = businessPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerBusiness(name, category, address, phone, email, username, password)
            } else {
                Toast.makeText(this, "Por favor llena todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para mostrar el TimePicker con AM/PM
    private fun mostrarTimePickerAMPM(onTimeSet: (Int, Int, String) -> Unit) {
        val calendar = Calendar.getInstance()
        val horaActual = calendar.get(Calendar.HOUR_OF_DAY)
        val minutoActual = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(this, { _, hora, minuto ->
            val amPm = if (hora < 12) "AM" else "PM"
            val horaEnFormato12 = if (hora % 12 == 0) 12 else hora % 12
            onTimeSet(horaEnFormato12, minuto, amPm)
        }, horaActual, minutoActual, false) // false para formato de 12 horas (AM/PM)

        timePicker.show()
    }

    // Método para formatear la hora y asegurar el formato correcto
    private fun formatearHora(hora: Int, minuto: Int, amPm: String): String {
        val minutoFormateado = if (minuto < 10) "0$minuto" else "$minuto"
        return "$hora:$minutoFormateado $amPm"
    }

    // Método para registrar el negocio
    private fun registerBusiness(
        name: String,
        category: String,
        address: String,
        phone: String,
        email: String,
        username: String,
        password: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val business = hashMapOf(
                        "name" to name,
                        "category" to category,
                        "address" to address,
                        "phone" to phone,
                        "email" to email,
                        "userId" to userId,
                        "workHours" to horariosLaborales // Guardar horarios laborales
                    )

                    // Guardar datos en Firestore
                    db.collection("Businesses").add(business)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Negocio registrado exitosamente", Toast.LENGTH_SHORT).show()

                            // Subir logo a Firebase Storage
                            logoUri?.let { uri ->
                                val storageRef = storage.reference.child("business_logos/${userId}.jpg")
                                storageRef.putFile(uri)
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al registrar el negocio: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Error de autenticación: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Manejo de resultado de selección de imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            logoUri = data?.data
            Toast.makeText(this, "Logo seleccionado", Toast.LENGTH_SHORT).show()
        }
    }
}
