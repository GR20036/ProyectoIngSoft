package com.example.agendify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard_Usuario extends AppCompatActivity {

    // Declarar todas las vistas del dashboard
    private TextView txtCitasProximas, txtDetalleCitas, txtHistorialCitas, txtDetalleHistorial, txtNotificaciones, txtDetalleNotificaciones, txtAccesosRapidos, txt_user_name;
    private Button btnCancelarCita, btnReprogramarCita, btnRepetirCita, btnvernegocios, btnContactarSoporte, btnLogout;
    private CardView cardCitasProximas, cardHistorialCitas, cardNotificaciones, cardAccesosRapidos;

    // Firebase variables
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard_usuario);

        // Configurar EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase Auth y Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar todas las vistas
        txtCitasProximas = findViewById(R.id.txt_citas_proximas);
        txtDetalleCitas = findViewById(R.id.txt_detalle_citas);
        txtHistorialCitas = findViewById(R.id.txt_historial_citas);
        txtDetalleHistorial = findViewById(R.id.txt_detalle_historial);
        txtNotificaciones = findViewById(R.id.txt_notificaciones);
        txtDetalleNotificaciones = findViewById(R.id.txt_detalle_notificaciones);
        txtAccesosRapidos = findViewById(R.id.txt_accesos_rapidos);
        txt_user_name = findViewById(R.id.txt_user_name);

        btnCancelarCita = findViewById(R.id.btn_cancelar_cita);
        btnReprogramarCita = findViewById(R.id.btn_reprogramar_cita);
        btnRepetirCita = findViewById(R.id.btn_repetir_cita);
        btnvernegocios = findViewById(R.id.btn_ver_negocios);
        btnContactarSoporte = findViewById(R.id.btn_contactar_soporte);
        btnLogout = findViewById(R.id.btn_logout); // Inicializa el botón de cerrar sesión

        cardCitasProximas = findViewById(R.id.card_citas_proximas);
        cardHistorialCitas = findViewById(R.id.card_historial_citas);
        cardNotificaciones = findViewById(R.id.card_notificaciones);
        cardAccesosRapidos = findViewById(R.id.card_accesos_rapidos);

        // Implementar la lógica para los botones
        btnCancelarCita.setOnClickListener(v -> {
            // Acción para cancelar la cita
        });

        btnReprogramarCita.setOnClickListener(v -> {
            // Acción para reprogramar la cita
        });

        btnRepetirCita.setOnClickListener(v -> {
            // Acción para repetir una cita
        });

        btnvernegocios.setOnClickListener(v -> {
            // Acción para consultar servicios disponibles
        });

        btnContactarSoporte.setOnClickListener(v -> {
            // Acción para contactar con soporte
        });

        // Obtener el nombre del usuario y mostrarlo en txt_user_name
        setUserName();

        // Funcionalidad del botón de cerrar sesión
        btnLogout.setOnClickListener(v -> {
            // Cerrar sesión del usuario
            auth.signOut();

            // Redirigir al usuario a la actividad de inicio de sesión
            Intent intent = new Intent(Dashboard_Usuario.this, Login_Usuario.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar el stack de actividades
            startActivity(intent);
            finish(); // Cerrar la actividad actual
        });
    }

    // Función para obtener el nombre del usuario desde Firestore y mostrarlo en el TextView

    private void setUserName() {
        String userId = auth.getCurrentUser().getUid(); // Obtener el UID del usuario actual
        Log.d("Dashboard_Usuario", "User ID: " + userId); // Log para verificar el UID

        db.collection("Users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String userName = documentSnapshot.getString("name"); // Obtener el campo "name" del usuario
                Log.d("Dashboard_Usuario", "Nombre obtenido: " + userName); // Log para verificar el nombre obtenido
                txt_user_name.setText(userName); // Establecer el nombre del usuario en el TextView
            } else {
                Log.d("Dashboard_Usuario", "El documento no existe"); // Log si el documento no existe
                txt_user_name.setText("Usuario"); // Mostrar "Usuario" si no se encuentra el nombre
            }
        }).addOnFailureListener(e -> {
            Log.e("Dashboard_Usuario", "Error al obtener el documento", e); // Log de error si falla la consulta
            txt_user_name.setText("Usuario"); // En caso de error, mostrar un texto por defecto
        });
    }
}
