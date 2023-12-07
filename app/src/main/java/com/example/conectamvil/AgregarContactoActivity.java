package com.example.conectamvil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class AgregarContactoActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextCorreo;
    private Button btnGuardar;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnBack = findViewById(R.id.btnBack);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarContacto();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para volver atrás (a la actividad ContactosActivity)
                Intent intent = new Intent(AgregarContactoActivity.this, ContactosActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void guardarContacto() {
        // Obtiene los datos del nuevo contacto
        String nombre = editTextNombre.getText().toString();
        String correo = editTextCorreo.getText().toString();

        // Obtén una referencia a la base de datos de Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Crea un nuevo nodo para almacenar los contactos (puedes cambiar "contactos" por el nombre que prefieras)
        DatabaseReference contactosReference = databaseReference.child("contactos");

        // Crea un nuevo ID único para el contacto
        String contactoId = contactosReference.push().getKey();

        // Crea un objeto Map para almacenar los datos del contacto
        Map<String, Object> contacto = new HashMap<>();
        contacto.put("nombre", nombre);
        contacto.put("correo", correo);

        // Guarda los datos del contacto en la base de datos bajo el ID único
        contactosReference.child(contactoId).setValue(contacto);

        // En este ejemplo, devolvemos los datos a la actividad principal
        Intent intent = new Intent();
        intent.putExtra("nombre", nombre);
        intent.putExtra("correo", correo);
        setResult(RESULT_OK, intent);
        finish();
    }
}
