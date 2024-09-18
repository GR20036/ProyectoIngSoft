package com.example.agendify

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import java.util.Calendar

class configuracion_horario : AppCompatActivity() {

    private lateinit var startLunesButton: Button
    private lateinit var endLunesButton: Button
    private lateinit var switchLunes: Switch
    private lateinit var LunesContainer: LinearLayout
    private lateinit var startMartesButton: Button
    private lateinit var endMartesButton: Button
    private lateinit var switchMartes: Switch
    private lateinit var MartesContainer: LinearLayout
    private lateinit var startMiercolesButton: Button
    private lateinit var endMiercolesButton: Button
    private lateinit var switchMiercoles: Switch
    private lateinit var MiercolesContainer: LinearLayout
    private lateinit var startJuevesButton: Button
    private lateinit var endJuevesButton: Button
    private lateinit var switchJueves: Switch
    private lateinit var JuevesContainer: LinearLayout
    private lateinit var startViernesButton: Button
    private lateinit var endViernesButton: Button
    private lateinit var switchViernes: Switch
    private lateinit var ViernesContainer: LinearLayout
    private lateinit var startSabadoButton: Button
    private lateinit var endSabadoButton: Button
    private lateinit var switchSabado: Switch
    private lateinit var SabadoContainer: LinearLayout
    private lateinit var startDomingoButton: Button
    private lateinit var endDomingoButton: Button
    private lateinit var switchDomingo: Switch
    private lateinit var DomingoContainer: LinearLayout

    private var startTimeLunes: String = ""
    private var endTimeLunes: String = ""
    private var startTimeMartes: String = ""
    private var endTimeMartes: String = ""
    private var startTimeMiercoles: String = ""
    private var endTimeMiercoles: String = ""
    private var startTimeJueves: String = ""
    private var endTimeJueves: String = ""
    private var startTimeViernes: String = ""
    private var endTimeViernes: String = ""
    private var startTimeSabado: String = ""
    private var endTimeSabado: String = ""
    private var startTimeDomingo: String = ""
    private var endTimeDomingo: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_configuracion_horario)

        switchLunes = findViewById(R.id.switchLunes)
        LunesContainer = findViewById(R.id.timeLunesContainer)
        startLunesButton = findViewById(R.id.startLunesButton)
        endLunesButton = findViewById(R.id.endLunesButton)

        // Listener para el switch
        switchLunes.setOnCheckedChangeListener { _, isChecked ->
            LunesContainer.isVisible = isChecked
        }

        // Selección de hora de inicio
        startLunesButton.setOnClickListener {
            pickTime { time ->
                startTimeLunes = time
                startLunesButton.text = "Inicio: $time"
            }
        }

        // Selección de hora de fin
        endLunesButton.setOnClickListener {
            pickTime { time ->
                endTimeLunes = time
                endLunesButton.text = "Fin: $time"
            }
        }

        // Botón para guardar el horario
        val saveScheduleButton = findViewById<Button>(R.id.saveScheduleButton)
        saveScheduleButton.setOnClickListener {
            if (switchLunes.isChecked) {
                if (startTimeLunes.isNotEmpty() && endTimeLunes.isNotEmpty()) {
                    saveSchedule("Lunes", startTimeLunes, endTimeLunes)
                } else {
                    Toast.makeText(this, "Por favor selecciona el horario completo para el lunes", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Método para abrir el TimePickerDialog
    private fun pickTime(onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(formattedTime)
        }, hour, minute, false).show() // `false` para mostrar AM/PM
    }

    // Método para guardar el horario en la base de datos (aquí puedes implementar la lógica)
    private fun saveSchedule(day: String, startTime: String, endTime: String) {
        // Lógica para guardar el horario en la base de datos
        Toast.makeText(this, "$day: $startTime - $endTime guardado", Toast.LENGTH_SHORT).show()
    }
    }