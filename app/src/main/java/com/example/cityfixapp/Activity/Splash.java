package com.example.cityfixapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixapp.R;

public class Splash extends AppCompatActivity {

    //Clase que mostrara la pantalla de incio de la aplicacion (se muestra el logo de la app)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Usar un Handler para retrasar la transición a la pantalla de inicio
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash.this, Login.class);
            startActivity(intent);
            finish();
        }, 2000); // 2 segundos de espera
    }

}
