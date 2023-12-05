package com.example.conectamvil;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterLoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnRegister;
    private Button btnLogin;
    private Button btnGoogleSignIn;

    // Add these for Google Sign-In
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
        Log.d("Tag", "onCreate");

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Google Sign-In options
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Initialize Google Sign-In client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Obtain references to UI elements
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);

        // Register User with Email
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registro exitoso
                                    Toast.makeText(RegisterLoginActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                    // Aquí podrías dirigir al usuario a la actividad MenuPrincipal si lo deseas
                                    // por ejemplo, usando un Intent
                                    Intent intent = new Intent(RegisterLoginActivity.this, MenuPrincipal.class);
                                    startActivity(intent);
                                } else {
                                    // Si el registro falla, muestra un mensaje al usuario
                                    Toast.makeText(RegisterLoginActivity.this, "Error en el registro: " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        // Login with Email
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Inicio de sesión exitoso
                                    Toast.makeText(RegisterLoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                    // Dirige al usuario a la actividad MenuPrincipal
                                    Intent intent = new Intent(RegisterLoginActivity.this, MenuPrincipal.class);
                                    startActivity(intent);
                                } else {
                                    // Si el inicio de sesión falla, muestra un mensaje al usuario
                                    Toast.makeText(RegisterLoginActivity.this, "Error en el inicio de sesión: " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Google Sign-In
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Google Sign-In Intent
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    // Handle result from Google Sign-In Intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Tag", "onActivityResult");

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    // Handle Google Sign-In result
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d("Tag", "handleSignInResult");

        try {
            GoogleSignInAccount googleSignInAccount = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(googleSignInAccount);
        } catch (ApiException e) {
            Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
            Log.e("Tag", "Google Sign-In failed", e);
        }
    }

    // Authenticate with Firebase using Google credentials
    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {
        Log.d("Tag", "firebaseAuthWithGoogle");

        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Google Sign-In successful
                            Toast.makeText(RegisterLoginActivity.this, "Google Sign-In successful", Toast.LENGTH_SHORT).show();
                        } else {
                            // Google Sign-In failed
                            Toast.makeText(RegisterLoginActivity.this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
                            Log.e("Tag", "Google Sign-In failed", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Tag", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Tag", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Tag", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Tag", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Tag", "onDestroy");
    }
}
