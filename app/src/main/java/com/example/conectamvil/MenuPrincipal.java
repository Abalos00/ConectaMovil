package com.example.conectamvil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipal extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;

    private TextView txtNombreUsuario;
    private ImageView imgFotoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Button btnVerContactos = findViewById(R.id.btnVerContactos);
        Button btnCambiarPerfil = findViewById(R.id.btnCambiarPerfil);
        Button btnEntrarChat = findViewById(R.id.btnEntrarChat);

        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        imgFotoUsuario = findViewById(R.id.imgFotoUsuario);

        // Obtén la información del usuario desde donde la estés almacenando
        obtenerDatosDeUsuario();

        // Acción al hacer clic en Ver Contactos
        btnVerContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementa la lógica para ver contactos
                Intent intent = new Intent(MenuPrincipal.this, ContactosActivity.class);
                startActivity(intent);
            }
        });

        // Acción al hacer clic en Cambiar Perfil
        btnCambiarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementa la lógica para cambiar de perfil
                Intent intent = new Intent(MenuPrincipal.this, PerfilActivity.class);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        // Acción al hacer clic en Entrar al Chat
        btnEntrarChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementa la lógica para entrar al chat
                // Por ejemplo, puedes iniciar otra actividad para el chat
                startActivity(new Intent(MenuPrincipal.this, ChatActivity.class));
            }
        });
    }

    private void obtenerDatosDeUsuario() {
        // Obtén el ID del usuario actual (esto podría depender de cómo manejas la autenticación en tu aplicación)
        String userId = "WWgNlVBAK9Xs50gMYIWfA8zCJ0V2";

        // Obtiene una referencia al nodo del usuario en Firebase
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(userId);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verifica si los nodos necesarios existen
                if (dataSnapshot.child("nombre").exists() && dataSnapshot.child("imagenURL").exists()) {
                    // Obtiene los valores necesarios y actualiza la interfaz de usuario
                    String nombreUsuario = dataSnapshot.child("nombre").getValue(String.class);
                    String imagenUrl = dataSnapshot.child("imagenURL").getValue(String.class);

                    // Actualiza la interfaz de usuario
                    txtNombreUsuario.setText(nombreUsuario);

                    // Utiliza Glide o Picasso para cargar la imagen desde la URL
                    Glide.with(MenuPrincipal.this).load(imagenUrl).into(imgFotoUsuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Maneja el error al acceder a la base de datos
                Toast.makeText(MenuPrincipal.this, "Error al obtener datos de usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            // Actualiza los datos en la interfaz de usuario con los nuevos valores
            String nuevoNombre = data.getStringExtra("nuevoNombre");

            // Verifica si la clave "uriImagen" está presente en los extras
            if (data.hasExtra("uriImagen")) {
                String uriImagen = data.getStringExtra("uriImagen");

                // Puedes utilizar Glide o Picasso para cargar la imagen desde la URI
                // Aquí asumiré que la URI es válida, pero verifica si es así antes de usarla
                Uri nuevaUri = Uri.parse(uriImagen);
                imgFotoUsuario.setImageURI(nuevaUri);
            }

            // Actualiza el nombre en la interfaz de usuario
            txtNombreUsuario.setText(nuevoNombre);

            // Ahora, puedes guardar estos cambios en Firebase si es necesario
            // guardarCambiosEnFirebase(nuevoNombre, nuevaUri);
        }
    }
}
