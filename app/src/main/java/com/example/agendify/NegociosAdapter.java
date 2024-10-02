package com.example.agendify;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.api.Authentication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class NegociosAdapter extends RecyclerView.Adapter<NegociosAdapter.NegociosViewHolder> {

    private List<Negocio> negocioList;
    public Button btnAgendarCita;
    public NegociosAdapter(List<Negocio> negocioList) {
        this.negocioList = negocioList;
    }

    @NonNull
    @Override
    public NegociosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_negocio, parent, false);
        return new NegociosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NegociosViewHolder holder, int position) {
        // Obtener el negocio de la posición actual
        Negocio negocio = negocioList.get(position);

        // Establecer los valores del nombre y la descripción en los TextViews
        holder.txtNombre.setText(negocio.getNombre());
        holder.txtDescripcion.setText(negocio.getDescripcion());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String nombreUsuario = "";
        db.collection("Users")
                .whereEqualTo("userId", auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Suponiendo que solo hay un documento que coincide
                        for (QueryDocumentSnapshot document : querySnapshot) {
                        }
                    } else {
                        Log.d("NombreUsuario", "No se encontró el usuario.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("NombreUsuario", "Error al obtener el usuario", e);
                });


        // Cargar el logo usando Glide desde una URL
        Glide.with(holder.itemView.getContext())
                .load(negocio.getLogoUrl()) // URL del logo
                .placeholder(R.drawable.place_holder) // Imagen temporal mientras carga
                .error(R.drawable.error_logo) // Imagen en caso de error
                .into(holder.imgLogoNegocio); // Cargar en el ImageView
        // Agendar cita: al hacer clic en el botón "Agendar Cita"
        holder.btnAgendarCita.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), CrearCita_Usuario.class);//temporal
            intent.putExtra("negocioId", negocio.getId());  // Pasar el ID del negocio
            holder.itemView.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return negocioList.size();
    }

    static class NegociosViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDescripcion;
        ImageView imgLogoNegocio;
        Button btnAgendarCita;

        public NegociosViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreNegocio);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcionNegocio);
            imgLogoNegocio = itemView.findViewById(R.id.imgLogoNegocio); // Asegúrate de inicializar el ImageView
            btnAgendarCita = itemView.findViewById(R.id.btnAgendarCita);
        }
    }
}

