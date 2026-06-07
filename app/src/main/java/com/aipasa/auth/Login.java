package com.aipasa.auth;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aipasa.MainBab;
import com.aipasa.R;
import com.aipasa.configuracion.PoliticaPrivacidadActivity;
import com.aipasa.configuracion.TerminosCondicionesActivity;
import com.aipasa.main.PreferenciasActivity;
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

    private static final String PREFS = "petfect_prefs";
    private static final String PREF_PREFERENCIAS_CONFIGURADAS = "preferencias_configuradas";
    private static final String PREF_TERMINOS_ACEPTADOS = "terminos_aceptados";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);

        mAuth = FirebaseAuth.getInstance();

        if (!terminosAceptados()) {
            mostrarDialogTerminos();
        }

        if (mAuth.getCurrentUser() != null && terminosAceptados()) {
            abrirPantallaDespuesDeLogin();
            return;
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton btnGoogle = findViewById(R.id.btnGoogle);

        if (btnGoogle != null) {
            btnGoogle.setOnClickListener(v -> {
                if (!terminosAceptados()) {
                    mostrarDialogTerminos();
                    return;
                }

                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            });
        }
    }

    public void SignLogin(View view) {
        if (!terminosAceptados()) {
            mostrarDialogTerminos();
            return;
        }

        String email = etUser.getText().toString().trim();
        String password = etPass.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                this,
                                "Inicio de sesión correcto",
                                Toast.LENGTH_SHORT
                        ).show();

                        abrirPantallaDespuesDeLogin();

                    } else {

                        Toast.makeText(
                                this,
                                "Error de autenticación",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    public void OpenSignup(View view) {
        if (!terminosAceptados()) {
            mostrarDialogTerminos();
            return;
        }

        startActivity(new Intent(this, SignUp.class));
    }

    private void abrirPantallaDespuesDeLogin() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        boolean preferenciasConfiguradas =
                prefs.getBoolean(PREF_PREFERENCIAS_CONFIGURADAS, false);

        Intent intent;

        if (preferenciasConfiguradas) {
            intent = new Intent(this, MainBab.class);
        } else {
            intent = new Intent(this, PreferenciasActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean terminosAceptados() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        return prefs.getBoolean(PREF_TERMINOS_ACEPTADOS, false);
    }

    private void guardarTerminosAceptados() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        prefs.edit()
                .putBoolean(PREF_TERMINOS_ACEPTADOS, true)
                .apply();
    }

    private void mostrarDialogTerminos() {
        Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_terms);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView tvTerminos = dialog.findViewById(R.id.tvTerminos);
        TextView tvPrivacidad = dialog.findViewById(R.id.tvPrivacidad);
        CheckBox checkAccept = dialog.findViewById(R.id.checkAccept);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);

        if (tvTerminos != null) {
            tvTerminos.setOnClickListener(v -> {
                Intent intent = new Intent(Login.this, TerminosCondicionesActivity.class);
                startActivity(intent);
            });
        }

        if (tvPrivacidad != null) {
            tvPrivacidad.setOnClickListener(v -> {
                Intent intent = new Intent(Login.this, PoliticaPrivacidadActivity.class);
                startActivity(intent);
            });
        }

        if (btnAccept != null) {
            btnAccept.setEnabled(false);
            btnAccept.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gris))
            );
        }

        if (checkAccept != null && btnAccept != null) {
            checkAccept.setOnCheckedChangeListener((buttonView, isChecked) -> {
                btnAccept.setEnabled(isChecked);

                if (isChecked) {
                    checkAccept.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
                    btnAccept.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.cian_botones))
                    );
                } else {
                    btnAccept.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gris))
                    );
                }
            });
        }

        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }

        if (btnAccept != null) {
            btnAccept.setOnClickListener(v -> {
                if (checkAccept != null && !checkAccept.isChecked()) {
                    checkAccept.setTextColor(ContextCompat.getColor(this, R.color.coral_alertasimp_errores));

                    Toast.makeText(
                            this,
                            getString(R.string.debes_aceptar_terminos),
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                guardarTerminosAceptados();
                dialog.dismiss();

                if (mAuth.getCurrentUser() != null) {
                    abrirPantallaDespuesDeLogin();
                }
            });
        }

        dialog.show();

        Window shownWindow = dialog.getWindow();

        if (shownWindow != null) {
            shownWindow.setLayout(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
            );
        }
    }

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
                Toast.makeText(
                        this,
                        "Error Google Sign-In",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential =
                GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        abrirPantallaDespuesDeLogin();

                    } else {

                        Toast.makeText(
                                this,
                                "Error autenticando con Google",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}