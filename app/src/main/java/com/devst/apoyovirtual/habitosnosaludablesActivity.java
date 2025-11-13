package com.devst.apoyovirtual;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class habitosnosaludablesActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habitosnosaludables);

        // Configurar botón Regresar - CORREGIDO
        Button btnRegresar = findViewById(R.id.btnRegresarnosaludables); // ID correcto
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Regresar a la activity anterior
            }
        });
    }
}