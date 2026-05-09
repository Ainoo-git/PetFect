package com.aipasa.splash;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.aipasa.auth.Login;

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
        ScrollView scrollView = dialog.findViewById(R.id.scrollView);

        TextView tvTerminos = dialog.findViewById(R.id.tvTerminos);
        TextView tvPrivacidad = dialog.findViewById(R.id.tvPrivacidad);

        final boolean[] scrolled = {false};
        final boolean[] checked = {false};

        // Abrir términos
        tvTerminos.setOnClickListener(v -> {
            Intent intent = new Intent(Splash.this, TerminosActivity.class);
            startActivity(intent);
        });

        // Abrir privacidad
        tvPrivacidad.setOnClickListener(v -> {
            Intent intent = new Intent(Splash.this, PrivacidadActivity.class);
            startActivity(intent);
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {

            View content = scrollView.getChildAt(0);

            int diff = content.getBottom()
                    - (scrollView.getHeight() + scrollView.getScrollY());

            if (diff <= 0) {
                scrolled[0] = true;
            }

            btnAccept.setEnabled(scrolled[0] && checked[0]);
        });

        check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checked[0] = isChecked;
            btnAccept.setEnabled(scrolled[0] && checked[0]);
        });

        btnAccept.setOnClickListener(v -> {

            getSharedPreferences("petfect", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("terms_accepted", true)
                    .apply();

            dialog.dismiss();
            goToApp();
        });

        btnCancel.setOnClickListener(v -> finish());

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