package com.example.agendify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class NegociosDisponiblesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private NegociosAdapter negociosAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private List<Negocio> negocioList = new ArrayList<>(); // Lista para almacenar los negocios

    // Firebase Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocios_disponibles);

        // Inicializar el RecyclerView y otras vistas
        recyclerView = findViewById(R.id.recyclerViewNegocios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar Toolbar y DrawerLayout
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Configurar el DrawerToggle para abrir/cerrar el menú
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Cargar los negocios desde Firebase Firestore
        cargarNegocios();
    }

    private void cargarNegocios() {
        db.collection("Businesses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    negocioList.clear(); // Limpiar la lista de negocios antes de añadir nuevos

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String nombre = document.getString("nombre_negocio");
                        String telefono = document.getString("telefono");
                        String logoUrl = document.getString("logo_url");

                        if (nombre != null && telefono != null) {
                            Negocio negocio = new Negocio(nombre, telefono, logoUrl); // URL temporal
                            negocioList.add(negocio); // Agregar el negocio a la lista
                        }
                    }

                    // Verificar que hay negocios en la lista
                    if (!negocioList.isEmpty()) {
                        // Asignar el adaptador al RecyclerView
                        if (negociosAdapter == null) {
                            negociosAdapter = new NegociosAdapter(negocioList);
                            recyclerView.setAdapter(negociosAdapter);
                        } else {
                            // Notificar al adaptador que los datos han cambiado si ya está asignado
                            negociosAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("Firestore", "No hay negocios disponibles para mostrar.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al obtener los negocios", e);
                });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Ir al Home
            Intent intent = new Intent(this, Dashboard_Usuario.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            // Navegar a Perfil
        } else if (id == R.id.nav_settings) {
            // Navegar a Ajustes
        } else if (id == R.id.nav_logout) {
            //cerramos sesion
            Intent intent = new Intent(this, primera_vista.class);
            startActivity(intent);


        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
