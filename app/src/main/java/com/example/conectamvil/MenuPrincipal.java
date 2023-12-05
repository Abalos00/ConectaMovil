package com.example.conectamvil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Button btnVerContactos = findViewById(R.id.btnVerContactos);
        Button btnCambiarPerfil = findViewById(R.id.btnCambiarPerfil);
        Button btnEntrarChat = findViewById(R.id.btnEntrarChat);

        // Acción al hacer clic en Ver Contactos
        btnVerContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementa la lógica para ver contactos
            }
        });

        // Acción al hacer clic en Cambiar Perfil
        btnCambiarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementa la lógica para cambiar de perfil
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
}
