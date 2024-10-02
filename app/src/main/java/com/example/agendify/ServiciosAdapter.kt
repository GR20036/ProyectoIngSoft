package com.example.agendify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ServiciosAdapter(private val serviciosList: List<Servicio>) :
    RecyclerView.Adapter<ServiciosAdapter.ServicioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicioViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_servicio, parent, false)
        return ServicioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ServicioViewHolder, position: Int) {
        val servicio = serviciosList[position]
        holder.bind(servicio)
    }

    override fun getItemCount(): Int {
        return serviciosList.size
    }

    class ServicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombreServicio: TextView = itemView.findViewById(R.id.tvNombreServicio)
        private val tvDescripcionServicio: TextView = itemView.findViewById(R.id.tvDescripcionServicio)
        private val tvTiempoAproxServicio: TextView = itemView.findViewById(R.id.tvTiempoAproxServicio)
        private val tvPrecioServicio: TextView = itemView.findViewById(R.id.tvPrecioServicio)

        fun bind(servicio: Servicio) {
            tvNombreServicio.text = servicio.nombre
            tvDescripcionServicio.text = servicio.descripcion
            tvTiempoAproxServicio.text = "${servicio.tiempoAprox} minutos"
            tvPrecioServicio.text = "$${servicio.precio}"
        }
    }
}
