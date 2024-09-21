package com.example.agendify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CitasAdapter(
    private val citasList: List<Cita>,
    private val onItemClick: (Cita) -> Unit
) : RecyclerView.Adapter<CitasAdapter.CitaViewHolder>() {

    inner class CitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCliente = itemView.findViewById<TextView>(R.id.tvCliente)
        private val tvServicio = itemView.findViewById<TextView>(R.id.tvServicio)
        private val tvFecha = itemView.findViewById<TextView>(R.id.tvFecha)
        private val tvHora = itemView.findViewById<TextView>(R.id.tvHora)

        fun bind(cita: Cita) {
            tvCliente.text = cita.cliente
            tvServicio.text = cita.servicio
            tvFecha.text = cita.fecha
            tvHora.text = cita.hora

            // Manejar clic en la cita
            itemView.setOnClickListener {
                onItemClick(cita)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        holder.bind(citasList[position])
    }

    override fun getItemCount(): Int = citasList.size
}
