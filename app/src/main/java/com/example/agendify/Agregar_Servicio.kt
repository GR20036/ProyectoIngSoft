package com.example.agendify

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class Agregar_Servicio : DialogFragment() {

    private lateinit var onSaveListener: (String, String, Int, Double) -> Unit

    fun setOnSaveListener(listener: (String, String, Int, Double) -> Unit) {
        onSaveListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_agregar_servicio, null)

        val serviceName = view.findViewById<EditText>(R.id.service_name)
        val serviceDescription = view.findViewById<EditText>(R.id.service_description)
        val serviceDuration = view.findViewById<EditText>(R.id.service_duration)
        val servicePrice = view.findViewById<EditText>(R.id.service_price)

        view.findViewById<Button>(R.id.btn_save_service).setOnClickListener {
            val name = serviceName.text.toString()
            val description = serviceDescription.text.toString()
            val duration = serviceDuration.text.toString().toIntOrNull() ?: 0
            val price = servicePrice.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isNotEmpty()) {
                onSaveListener(name, description, duration, price)
                dismiss()
            } else {
                Toast.makeText(context, "Por favor, ingresa un nombre", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setView(view)
        return builder.create()
    }
}
