package com.example.conectamvil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtén una referencia al botón en la interfaz de usuario
        Button btnGoToRegisterLogin = findViewById(R.id.btnGoToRegisterLogin);

        // Establece un listener de clic para el botón
        btnGoToRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterLoginActivity.class);
                startActivity(intent);

                // Aplicar la animación
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}
