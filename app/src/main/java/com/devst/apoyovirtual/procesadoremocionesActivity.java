package com.devst.apoyovirtual;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class procesadoremocionesActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.procesadoremociones);

        // Configurar botón Regresar
        Button btnRegresar = findViewById(R.id.btnRegresarprocesaremociones);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Regresar a la activity anterior
            }
        });
    }
}
