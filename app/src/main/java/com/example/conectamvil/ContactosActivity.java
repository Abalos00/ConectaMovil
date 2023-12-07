package com.example.conectamvil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactosActivity extends AppCompatActivity {

    private static final int CODIGO_AGREGAR_CONTACTO = 1;

    private EditText editTextSearch;
    private RecyclerView recyclerViewContacts;
    private Button btnAddContact;
    private Button btnBack;

    private ContactosAdapter contactosAdapter;
    private List<String> listaContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        // Obtener referencias a elementos de la interfaz
        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        btnAddContact = findViewById(R.id.btnAddContact);
        btnBack = findViewById(R.id.btnBack);

        // Configurar RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewContacts.setLayoutManager(layoutManager);

        // Configurar adaptador
        listaContactos = new ArrayList<>();  // Inicializar la lista
        contactosAdapter = new ContactosAdapter(listaContactos);
        recyclerViewContacts.setAdapter(contactosAdapter);

        // Aplicar decoración de elementos
        int espacioEntreElementos = 8; // Ajusta este valor según sea necesario
        recyclerViewContacts.addItemDecoration(new ItemDecoration(espacioEntreElementos));

        // Configurar acciones de búsqueda y agregar contacto (ejemplo básico)
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // No necesario para este ejemplo
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Filtrar la lista de contactos según el texto de búsqueda
                List<String> listaFiltrada = filtrarContactos(listaContactos, charSequence.toString());
                contactosAdapter.setListaContactos(listaFiltrada);
                contactosAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // String textoActual = editable.toString();  // No utilizado en este ejemplo
            }
        });

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para agregar un nuevo contacto
                abrirActividadAgregarContacto();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para volver atrás (a la actividad MenuPrincipal)
                Intent intent = new Intent(ContactosActivity.this, MenuPrincipal.class);
                startActivity(intent);
                finish();
            }
        });

        // Cargar la lista de contactos inicialmente
        cargarListaContactos();
    }

    private void cargarListaContactos() {
        DatabaseReference contactosReference = FirebaseDatabase.getInstance().getReference().child("contactos");

        contactosReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaContactos.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String nombreContacto = snapshot.child("nombre").getValue(String.class);
                    listaContactos.add(nombreContacto);
                }

                contactosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ContactosActivity.this, "Error al obtener datos de la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirActividadAgregarContacto() {
        Intent intent = new Intent(ContactosActivity.this, AgregarContactoActivity.class);
        startActivityForResult(intent, CODIGO_AGREGAR_CONTACTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_AGREGAR_CONTACTO && resultCode == RESULT_OK) {
            // Actualizar la lista de contactos después de agregar
            Toast.makeText(this, "Contacto agregado correctamente", Toast.LENGTH_SHORT).show();
            cargarListaContactos();
        }
    }

    private List<String> filtrarContactos(List<String> listaContactos, String filtro) {
        // En este ejemplo, se filtran los contactos que contienen el texto de búsqueda
        List<String> listaFiltrada = new ArrayList<>();
        for (String contacto : listaContactos) {
            if (contacto.toLowerCase().contains(filtro.toLowerCase())) {
                listaFiltrada.add(contacto);
            }
        }
        return listaFiltrada;
    }

    @Override
    public void onBackPressed() {
        // Lógica para volver atrás (a la actividad MenuPrincipal)
        super.onBackPressed();
        Intent intent = new Intent(ContactosActivity.this, MenuPrincipal.class);
        startActivity(intent);
        finish();
    }
}