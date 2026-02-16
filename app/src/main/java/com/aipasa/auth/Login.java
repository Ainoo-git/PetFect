package com.aipasa.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.aipasa.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    private TextInputEditText etUser;
    private TextInputEditText etPass;

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);

        mAuth = FirebaseAuth.getInstance();

        // Si ya está logueado, entrar directamente
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        // Configuración Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton btnGoogle = findViewById(R.id.btnGoogle);

        if (btnGoogle != null) {
            btnGoogle.setOnClickListener(v -> {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            });
        }
    }

    // LOGIN NORMAL
    public void SignLogin(View view) {

        String email = etUser.getText().toString().trim();
        String password = etPass.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(this,
                                "Inicio de sesión correcto",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(this, MainActivity.class));
                        finish();

                    } else {

                        Exception exception = task.getException();

                        if (exception != null &&
                                exception.getMessage() != null &&
                                exception.getMessage().contains("There is no user record")) {

                            Toast.makeText(this,
                                    "Usuario no registrado",
                                    Toast.LENGTH_SHORT).show();

                        } else if (exception != null &&
                                exception.getMessage() != null &&
                                exception.getMessage().contains("password is invalid")) {

                            Toast.makeText(this,
                                    "Contraseña incorrecta",
                                    Toast.LENGTH_SHORT).show();

                        } else if (exception != null &&
                                exception.getMessage() != null &&
                                exception.getMessage().contains("Google")) {

                            Toast.makeText(this,
                                    "Esta cuenta está registrada con Google. Usa el botón Google.",
                                    Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(this,
                                    "Error de autenticación",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

<<<<<<< HEAD
    // Botón SIGN UP
    public void OpenSignup(View view ) {
        Intent i = new Intent(Login.this, SignUp.class);
        startActivity(i);
=======
    // BOTÓN SIGN UP
    public void OpenSignup(View view) {
        startActivity(new Intent(this, SignUp.class));
    }

    // RESULTADO GOOGLE
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                Toast.makeText(this,
                        "Error Google Sign-In",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential =
                GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        startActivity(new Intent(this, MainActivity.class));
                        finish();

                    } else {

                        Toast.makeText(this,
                                "Error autenticando con Google",
                                Toast.LENGTH_SHORT).show();
                    }
                });
>>>>>>> ad696360465e74c90df5afb3f20792902b078af1
    }
}
