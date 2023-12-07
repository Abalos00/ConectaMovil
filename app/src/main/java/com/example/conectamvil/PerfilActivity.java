package com.example.conectamvil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PerfilActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextDescripcion;
    private Button btnCambiarFotoPerfil;
    private Button btnGuardarCambios;
    private Button btnCambiarCuenta;
    private Button btnVolverAtras; // Nuevo botón para volver atrás
    private ImageView imageViewPerfil;
    private static final int RESULT_LOAD_IMAGE = 1;
    private Uri selectedImage;
    private DatabaseReference userRef;
    private FirebaseUser currentUser;
    private StorageReference storageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // El usuario no está autenticado. Maneja esto según tus requisitos.
            finish();
            return;
        }

        userRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(currentUser.getUid());
        storageRef = FirebaseStorage.getInstance().getReference().child("profile_images").child(currentUser.getUid());

        // Obtén referencias a los elementos de la interfaz de usuario
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        btnCambiarFotoPerfil = findViewById(R.id.btnCambiarFotoPerfil);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        btnCambiarCuenta = findViewById(R.id.btnCambiarCuenta);
        btnVolverAtras = findViewById(R.id.btnVolverAtras); // Inicializa el nuevo botón
        imageViewPerfil = findViewById(R.id.imageViewPerfil);

        // Obtén la información actual del usuario desde Firebase
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombreUsuario = snapshot.child("nombre").getValue(String.class);
                    String descripcionUsuario = snapshot.child("descripcion").getValue(String.class);
                    String imagenURL = snapshot.child("imagenURL").getValue(String.class);

                    // Agrega logs para verificar los valores
                    Log.d("PerfilActivity", "Nombre de usuario: " + nombreUsuario);
                    Log.d("PerfilActivity", "Descripción de usuario: " + descripcionUsuario);
                    Log.d("PerfilActivity", "URL de imagen: " + imagenURL);

                    // Establece la información actual en los campos de texto
                    editTextNombre.setText(nombreUsuario);
                    editTextDescripcion.setText(descripcionUsuario);

                    // Muestra la imagen en imageViewPerfil usando Glide
                    if (imagenURL != null) {
                        selectedImage = Uri.parse(imagenURL);
                        Glide.with(PerfilActivity.this).load(selectedImage).into(imageViewPerfil);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Toast.makeText(PerfilActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Implementa la lógica para cambiar la foto de perfil (puedes usar un Intent para abrir la galería o la cámara)
        btnCambiarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la galería mediante un Intent
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoNombre = editTextNombre.getText().toString();
                String nuevaDescripcion = editTextDescripcion.getText().toString();

                // Actualiza los datos del usuario en Firebase
                userRef.child("nombre").setValue(nuevoNombre);
                userRef.child("descripcion").setValue(nuevaDescripcion);

                if (selectedImage != null) {
                    uploadImageToFirebaseStorage();
                } else {
                    // Abre MenuPrincipal con la información actualizada
                    openMenuPrincipal(nuevoNombre);
                }
            }
        });

        btnCambiarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilActivity.this, RegisterLoginActivity.class);
                startActivity(intent);
            }
        });

        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para volver atrás
                onBackPressed();
            }
        });
    }

    // Método para abrir MenuPrincipal con la información actualizada
    private void openMenuPrincipal(String nombreUsuario) {
        Intent intent = new Intent(PerfilActivity.this, MenuPrincipal.class);
        // Pasa el nombre de usuario como un extra al intent
        intent.putExtra("nombreUsuario", nombreUsuario);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();

            // Muestra la imagen en imageViewPerfil usando Glide
            Glide.with(this).load(selectedImage).into(imageViewPerfil);
        }
    }

    private void uploadImageToFirebaseStorage() {
        // Sube la imagen a Firebase Storage
        storageRef.putFile(selectedImage)
                .addOnSuccessListener(this, taskSnapshot -> {
                    // Obtiene la URL de la imagen almacenada
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Guarda la URL de la imagen
                        selectedImage = uri;

                        // Muestra la imagen en imageViewPerfil usando Glide
                        Glide.with(PerfilActivity.this).load(selectedImage).into(imageViewPerfil);

                        // Actualiza la URL de la imagen en Firebase Realtime Database
                        userRef.child("imagenURL").setValue(uri.toString());

                        // Finaliza la actividad
                        finish();
                    });
                })
                .addOnFailureListener(this, e -> {
                    // Maneja los errores al subir la imagen
                    Toast.makeText(PerfilActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                });
    }
}
