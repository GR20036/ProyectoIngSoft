package com.example.agendify

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_configuracion_horario)

        switchLunes = findViewById(R.id.switchLunes)
        LunesContainer = findViewById(R.id.timeLunesContainer)
        startLunesButton = findViewById(R.id.startLunesButton)
        endLunesButton = findViewById(R.id.endLunesButton)
        switchMartes = findViewById(R.id.switchMartes)
        MartesContainer = findViewById(R.id.timeMartesContainer)
        startMartesButton = findViewById(R.id.startMartesButton)
        endMartesButton = findViewById(R.id.endMartesButton)
        switchMiercoles = findViewById(R.id.switchMiercoles)
        MiercolesContainer = findViewById(R.id.timeMiercolesContainer)
        startMiercolesButton = findViewById(R.id.startMiercolesButton)
        endMiercolesButton = findViewById(R.id.endMiercolesButton)
        switchJueves = findViewById(R.id.switchJueves)
        JuevesContainer = findViewById(R.id.timeJuevesContainer)
        startJuevesButton = findViewById(R.id.startJuevesButton)
        endJuevesButton = findViewById(R.id.endJuevesButton)
        switchViernes = findViewById(R.id.switchViernes)
        ViernesContainer = findViewById(R.id.timeViernesContainer)
        startViernesButton = findViewById(R.id.startViernesButton)
        endViernesButton = findViewById(R.id.endViernesButton)
        switchSabado = findViewById(R.id.switchSabado)
        SabadoContainer = findViewById(R.id.timeSabadoContainer)
        startSabadoButton = findViewById(R.id.startSabadoButton)
        endSabadoButton = findViewById(R.id.endSabadoButton)
        switchDomingo = findViewById(R.id.switchDomingo)
        DomingoContainer = findViewById(R.id.timeDomingoContainer)
        startDomingoButton = findViewById(R.id.startDomingoButton)
        endDomingoButton = findViewById(R.id.endDomingoButton)

        // Recuperar los horarios pasados desde Registro_Negocio (o asignar valores predeterminados)
        var lunesInicio = intent.getStringExtra("lunes_inicio") ?: ""
        var lunesFin = intent.getStringExtra("lunes_fin") ?: ""
        var martesInicio = intent.getStringExtra("martes_inicio") ?: ""
        var martesFin = intent.getStringExtra("martes_fin") ?: ""
        var miercolesInicio = intent.getStringExtra("miercoles_inicio") ?: ""
        var miercolesFin = intent.getStringExtra("miercoles_fin") ?: ""
        var juevesInicio = intent.getStringExtra("jueves_inicio") ?: ""
        var juevesFin = intent.getStringExtra("jueves_fin") ?: ""
        var viernesInicio = intent.getStringExtra("viernes_inicio") ?: ""
        var viernesFin = intent.getStringExtra("viernes_fin") ?: ""
        var sabadoInicio = intent.getStringExtra("sabado_inicio") ?: ""
        var sabadoFin = intent.getStringExtra("sabado_fin") ?: ""
        var domingoInicio = intent.getStringExtra("domingo_inicio") ?: ""
        var domingoFin = intent.getStringExtra("domingo_fin") ?: ""

        // Poner los valores en los botones si no están vacíos
        if (lunesInicio.isNotEmpty()) startLunesButton.text = "Inicio: $lunesInicio"
        if (lunesFin.isNotEmpty()) {
            endLunesButton.text = "Fin: $lunesFin"
            switchLunes.isChecked = true
            LunesContainer.isVisible = true
        }
        if (martesInicio.isNotEmpty()) startMartesButton.text = "Inicio: $martesInicio"
        if (martesFin.isNotEmpty()) {
            endMartesButton.text = "Fin: $martesFin"
            switchMartes.isChecked = true
            MartesContainer.isVisible = true
        }
        if (miercolesInicio.isNotEmpty()) startMiercolesButton.text = "Inicio: $miercolesInicio"
        if (miercolesFin.isNotEmpty()) {
            endMiercolesButton.text = "Fin: $miercolesFin"
            switchMiercoles.isChecked = true
            MiercolesContainer.isVisible = true
        }
        if (juevesInicio.isNotEmpty()) startJuevesButton.text = "Inicio: $juevesInicio"
        if (juevesFin.isNotEmpty()) {
            endJuevesButton.text = "Fin: $juevesFin"
            switchJueves.isChecked = true
            JuevesContainer.isVisible = true
        }

        if (viernesInicio.isNotEmpty()) startViernesButton.text = "Inicio: $viernesInicio"
        if (viernesFin.isNotEmpty()) {
            endViernesButton.text = "Fin: $viernesFin"
            switchViernes.isChecked = true
            ViernesContainer.isVisible = true}
        if (sabadoInicio.isNotEmpty()) startSabadoButton.text = "Inicio: $sabadoInicio"
        if (sabadoFin.isNotEmpty()) {
            endSabadoButton.text = "Fin: $sabadoFin"
            switchSabado.isChecked = true
            SabadoContainer.isVisible = true}
        if (sabadoFin.isNotEmpty())
        if (domingoInicio.isNotEmpty()) startDomingoButton.text = "Inicio: $domingoInicio"
        if (domingoFin.isNotEmpty()) {
            endDomingoButton.text = "Fin: $domingoFin"
            switchDomingo.isChecked = true
            DomingoContainer.isVisible = true}

        // Listener para el switch
        switchLunes.setOnCheckedChangeListener { _, isChecked ->
            LunesContainer.isVisible = isChecked
        }

        // Selección de hora de inicio y fin para lunes
        startLunesButton.setOnClickListener {
            pickTime(lunesInicio) { time ->
                lunesInicio = time
                startLunesButton.text = "Inicio: $time"
            }
        }

        endLunesButton.setOnClickListener {
            pickTime(lunesFin) { time ->
                lunesFin = time
                endLunesButton.text = "Fin: $time"
            }
        }

        switchMartes.setOnCheckedChangeListener { _, isChecked ->
            MartesContainer.isVisible = isChecked
        }

        startMartesButton.setOnClickListener {
            pickTime(martesInicio) { time ->
                martesInicio = time
                startMartesButton.text = "Inicio: $time"
            }
        }

        endMartesButton.setOnClickListener {
            pickTime(martesFin) { time ->
                martesFin = time
                endMartesButton.text = "Fin: $time"
            }
        }

        switchMiercoles.setOnCheckedChangeListener { _, isChecked ->
            MiercolesContainer.isVisible = isChecked
        }

        startMiercolesButton.setOnClickListener {
            pickTime(miercolesInicio) { time ->
                miercolesInicio = time
                startMiercolesButton.text = "Inicio: $time"
            }
        }

        endMiercolesButton.setOnClickListener {
            pickTime(miercolesFin) { time ->
                miercolesFin = time
                endMiercolesButton.text = "Fin: $time"
            }
        }

        switchJueves.setOnCheckedChangeListener { _, isChecked ->
            JuevesContainer.isVisible = isChecked
        }

        startJuevesButton.setOnClickListener {
            pickTime(juevesInicio) { time ->
                juevesInicio = time
                startJuevesButton.text = "Inicio: $time"
            }
        }

        endJuevesButton.setOnClickListener {
            pickTime(juevesFin) { time ->
                juevesFin = time
                endJuevesButton.text = "Fin: $time"
            }
        }

        switchViernes.setOnCheckedChangeListener { _, isChecked ->
            ViernesContainer.isVisible = isChecked
        }

        startViernesButton.setOnClickListener {
            pickTime(viernesInicio) { time ->
                viernesInicio = time
                startViernesButton.text = "Inicio: $time"
            }
        }

        endViernesButton.setOnClickListener {
            pickTime(viernesFin) { time ->
                viernesFin = time
                endViernesButton.text = "Fin: $time"
            }
        }

        switchSabado.setOnCheckedChangeListener { _, isChecked ->
            SabadoContainer.isVisible = isChecked
        }

        startSabadoButton.setOnClickListener {
            pickTime(sabadoInicio) { time ->
                sabadoInicio = time
                startSabadoButton.text = "Inicio: $time"
            }
        }

        endSabadoButton.setOnClickListener {
            pickTime(sabadoFin) { time ->
                sabadoFin = time
                endSabadoButton.text = "Fin: $time"
            }
        }

        switchDomingo.setOnCheckedChangeListener { _, isChecked ->
            DomingoContainer.isVisible = isChecked
        }

        startDomingoButton.setOnClickListener {
            pickTime(domingoInicio) { time ->
                domingoInicio = time
                startDomingoButton.text = "Inicio: $time"
            }
        }

        endDomingoButton.setOnClickListener {
            pickTime(domingoFin) { time ->
                domingoFin = time
                endDomingoButton.text = "Fin: $time"
            }
        }

        // Botón para guardar el horario
        val saveScheduleButton = findViewById<Button>(R.id.btnGuardarHorario)
        saveScheduleButton.setOnClickListener {
            val resultIntent = Intent()
            if (switchLunes.isChecked && lunesInicio.isNotEmpty() && lunesFin.isNotEmpty()) {
                resultIntent.putExtra("lunes_inicio", lunesInicio)
                resultIntent.putExtra("lunes_fin", lunesFin)
            }
            if (switchMartes.isChecked && martesInicio.isNotEmpty() && martesFin.isNotEmpty()) {
                resultIntent.putExtra("martes_inicio", martesInicio)
                resultIntent.putExtra("martes_fin", martesFin)
            }
            if (switchMiercoles.isChecked && miercolesInicio.isNotEmpty() && miercolesFin.isNotEmpty()) {
                resultIntent.putExtra("miercoles_inicio", miercolesInicio)
                resultIntent.putExtra("miercoles_fin", miercolesFin)
            }
            if (switchJueves.isChecked && juevesInicio.isNotEmpty() && juevesFin.isNotEmpty()) {
                resultIntent.putExtra("jueves_inicio", juevesInicio)
                resultIntent.putExtra("jueves_fin", juevesFin)
            }
            if (switchViernes.isChecked && viernesInicio.isNotEmpty() && viernesFin.isNotEmpty()) {
                resultIntent.putExtra("viernes_inicio", viernesInicio)
                resultIntent.putExtra("viernes_fin", viernesFin)
            }
            if (switchSabado.isChecked && sabadoInicio.isNotEmpty() && sabadoFin.isNotEmpty()) {
                resultIntent.putExtra("sabado_inicio", sabadoInicio)
                resultIntent.putExtra("sabado_fin", sabadoFin)
            }
            if (switchDomingo.isChecked && domingoInicio.isNotEmpty() && domingoFin.isNotEmpty()) {
                resultIntent.putExtra("domingo_inicio", domingoInicio)
                resultIntent.putExtra("domingo_fin", domingoFin)
            }

            if (true) {
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Por favor selecciona un horario completo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickTime(defaultTime: String?, onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()

        // Variables para almacenar la hora y los minutos
        var hour: Int
        var minute: Int

        // Si hay una hora seleccionada previamente, usar esa como predeterminada
        if (defaultTime != null && defaultTime.isNotEmpty()) {
            try {
                val timeParts = defaultTime.split(" ")
                val hourMinuteParts = timeParts[0].split(":")
                val amPmPart = timeParts[1]

                // Extraer la hora y los minutos
                hour = hourMinuteParts[0].toInt()
                minute = hourMinuteParts[1].toInt()

                if (amPmPart.equals("PM", ignoreCase = true) && hour < 12) {
                    hour += 12
                } else if (amPmPart.equals("AM", ignoreCase = true) && hour == 12) {
                    hour = 0 // Medianoche
                }

            } catch (e: Exception) {
                // Si algo falla, usar la hora actual como predeterminada
                hour = calendar.get(Calendar.HOUR_OF_DAY)
                minute = calendar.get(Calendar.MINUTE)
            }
        } else {
            // Si no hay hora seleccionada, usar la hora actual
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
        }

        // Abrir el TimePickerDialog con la hora seleccionada o la actual
        val timePickerDialog = TimePickerDialog(this, { _, selectedHour: Int, selectedMinute: Int ->
            // Convertir a formato 12 horas
            val amPm = if (selectedHour >= 12) "PM" else "AM"
            val hourFormatted = if (selectedHour > 12) selectedHour - 12 else if (selectedHour == 0) 12 else selectedHour
            val time = String.format("%02d:%02d %s", hourFormatted, selectedMinute, amPm)
            onTimeSelected(time)
        }, hour, minute, false) //

        timePickerDialog.show()
    }

}
