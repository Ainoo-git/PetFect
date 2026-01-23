package com.aipasa;

import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Path;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageView logo = findViewById(R.id.img);

        // Obtener tamaño pantalla
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float ancho = dm.widthPixels;
        float alto = dm.heightPixels;

        // Path con entrada lateral, rebote y subida final
        Path path = new Path();

        // 1️⃣ Entrada desde lateral izquierda (fuera de pantalla)
        path.moveTo(-logo.getWidth(), alto * 0.75f); // empieza fuera de la pantalla a la izquierda

        // 2️⃣ Rebote tipo U horizontal hacia el centro
        path.cubicTo(ancho * 0.5f, alto * 0.8f,   // primer control punto (sube un poco)
                ancho * 0.4f, alto * 0.7f,   // segundo control punto (baja un poco)
                ancho * 0.5f, alto * 0.75f); // fin del rebote en el medio

        // 3️⃣ Subida final vertical hasta el centro final
        path.lineTo(ancho * 0.5f, alto * 0.4f);

        // Animación de posición siguiendo el path
        ObjectAnimator mover = ObjectAnimator.ofFloat(logo, View.X, View.Y, path);
        mover.setDuration(2200);
        mover.setInterpolator(new AccelerateDecelerateInterpolator());

        // Rotación opcional para más estilo
        ObjectAnimator rotar = ObjectAnimator.ofFloat(logo, "rotation", 0f, 360f);
        rotar.setDuration(2200);
        rotar.setInterpolator(new LinearInterpolator());

// AnimatorSet para ejecutar ambas animaciones juntas
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(mover, rotar);

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                Intent intent = new Intent(Splash.this, Login.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        animSet.start();


        animSet.start();

        // Fondo con Glide
        ImageView mSea = findViewById(R.id.backView);
        Glide.with(this)
                .load(R.drawable.imgfondo)
                .transition(DrawableTransitionOptions.withCrossFade(100))
                .centerCrop()
                .into(mSea);
    }
}
