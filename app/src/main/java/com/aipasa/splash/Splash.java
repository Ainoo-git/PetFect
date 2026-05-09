package com.aipasa.splash;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.aipasa.auth.Login;
import com.aipasa.configuracion.PoliticaPrivacidadActivity;
import com.aipasa.configuracion.TerminosCondicionesActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageView logo = findViewById(R.id.img);
        Animation starAnim = AnimationUtils.loadAnimation(this, R.anim.trans_icon);

        starAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                SharedPreferences prefs = getSharedPreferences("petfect", MODE_PRIVATE);
                boolean accepted = prefs.getBoolean("terms_accepted", false);

                if (!accepted) {
                    showTermsDialog();
                } else {
                    goToApp();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        logo.startAnimation(starAnim);
    }

    private void showTermsDialog() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_terms);
        dialog.setCancelable(false);

        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        CheckBox check = dialog.findViewById(R.id.checkAccept);

        TextView tvTerminos = dialog.findViewById(R.id.tvTerminos);
        TextView tvPrivacidad = dialog.findViewById(R.id.tvPrivacidad);

        // Desactivar aceptar al inicio
        btnAccept.setEnabled(false);

        // Abrir términos y condiciones
        tvTerminos.setOnClickListener(v -> {

            Intent intent = new Intent(
                    Splash.this,
                    TerminosCondicionesActivity.class
            );

            startActivity(intent);
        });

        // Abrir política de privacidad
        tvPrivacidad.setOnClickListener(v -> {

            Intent intent = new Intent(
                    Splash.this,
                    PoliticaPrivacidadActivity.class
            );

            startActivity(intent);
        });

        // Activar botón al marcar checkbox
        check.setOnCheckedChangeListener((buttonView, isChecked) -> {

            btnAccept.setEnabled(isChecked);
        });

        // Aceptar términos
        btnAccept.setOnClickListener(v -> {

            getSharedPreferences("petfect", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("terms_accepted", true)
                    .apply();

            dialog.dismiss();

            goToApp();
        });

        // Cancelar
        btnCancel.setOnClickListener(v -> {

            finish();
        });

        // Fondo transparente
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT)
            );
        }

        dialog.show();
    }

    private void goToApp() {
        Intent intent = new Intent(Splash.this, Login.class);
        startActivity(intent);
        overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
        finish();
    }
}