package com.devst.apoyovirtual;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class principalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        // Configurar botones de navegación
        setupNavigationButtons();
    }

    private void setupNavigationButtons() {
        // Botón Detector de Estado de Ánimo
        Button btnDetector = findViewById(R.id.detectorButton);
        btnDetector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(principalActivity.this, procesadoremocionesActivity.class);
                startActivity(intent);
            }
        });

        Button btnsomos = findViewById(R.id.somosButton);
        btnsomos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(principalActivity.this, quienessomosActivity.class);
                startActivity(intent);
            }
        });


        Button btnsaludables = findViewById(R.id.saludablesButton);
        btnsaludables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(principalActivity.this, habitossaludablesActivity.class);
                startActivity(intent);
            }
        });

        Button btnnosaludables = findViewById(R.id.nosaludablesButton);
        btnnosaludables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(principalActivity.this, habitosnosaludablesActivity.class);
                startActivity(intent);
            }
        });


        // Botón Enviar Mensaje
        Button btnmensaje = findViewById(R.id.mensajeButton);
        btnmensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(principalActivity.this, mensajeActivity.class);
                startActivity(intent);
            }
        });

        // Botón Referencias
        Button btnreferencias = findViewById(R.id.referenciasButton);
        btnreferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(principalActivity.this, referenciasActivity.class);
                startActivity(intent);
            }
        });

        Button btnCalendario = findViewById(R.id.calendarioButton); // Usa el ID del botón que definiste en principal.xml
        btnCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí asumimos que tienes una Activity llamada CalendarioActivity
                // Si tu Activity se llama diferente, cámbialo aquí.
                Intent intent = new Intent(principalActivity.this, calendarioActivity.class);
                startActivity(intent);
            }
        });

        Button btnEmociones = findViewById(R.id.emocionesButton);
        btnEmociones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(principalActivity.this, EmocionesActivity.class);
                startActivity(intent);
            }
        });
    }
}
