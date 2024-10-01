package com.example.agendify;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.cardview.widget.CardView;

public class Dashboard_Usuario extends AppCompatActivity {

    // Declarar todas las vistas del dashboard
    private TextView txtCitasProximas, txtDetalleCitas, txtHistorialCitas, txtDetalleHistorial, txtNotificaciones, txtDetalleNotificaciones, txtAccesosRapidos;
    private Button btnCancelarCita, btnReprogramarCita, btnRepetirCita, btnAgendarCita, btnConsultarServicios, btnContactarSoporte;
    private CardView cardCitasProximas, cardHistorialCitas, cardNotificaciones, cardAccesosRapidos;

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

        // Inicializar todas las vistas
        txtCitasProximas = findViewById(R.id.txt_citas_proximas);
        txtDetalleCitas = findViewById(R.id.txt_detalle_citas);
        txtHistorialCitas = findViewById(R.id.txt_historial_citas);
        txtDetalleHistorial = findViewById(R.id.txt_detalle_historial);
        txtNotificaciones = findViewById(R.id.txt_notificaciones);
        txtDetalleNotificaciones = findViewById(R.id.txt_detalle_notificaciones);
        txtAccesosRapidos = findViewById(R.id.txt_accesos_rapidos);

        btnCancelarCita = findViewById(R.id.btn_cancelar_cita);
        btnReprogramarCita = findViewById(R.id.btn_reprogramar_cita);
        btnRepetirCita = findViewById(R.id.btn_repetir_cita);
        btnAgendarCita = findViewById(R.id.btn_agendar_cita);
        btnConsultarServicios = findViewById(R.id.btn_consultar_servicios);
        btnContactarSoporte = findViewById(R.id.btn_contactar_soporte);

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

        btnAgendarCita.setOnClickListener(v -> {
            // Acción para agendar una nueva cita
        });

        btnConsultarServicios.setOnClickListener(v -> {
            // Acción para consultar servicios disponibles
        });

        btnContactarSoporte.setOnClickListener(v -> {
            // Acción para contactar con soporte
        });
    }
}
