package com.aipasa.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.aipasa.auth.Login;
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
        Animation starAnim = AnimationUtils.loadAnimation(this, R.anim.trans_icon);

        starAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Ir al Login cuando termine la animación
                Intent intent = new Intent(Splash.this, Login.class);
                startActivity(intent);
                overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        logo.startAnimation(starAnim);

        // Fondo con Glide
        ImageView mSea = findViewById(R.id.backView);
        Glide.with(this)
                .load(R.drawable.imgfondo)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(mSea);
    }
}