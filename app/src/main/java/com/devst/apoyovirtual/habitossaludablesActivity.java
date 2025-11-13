package com.devst.apoyovirtual;



import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class habitossaludablesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habitossaludables);

        // Configurar botón Regresar
        Button btnRegresar = findViewById(R.id.btnRegresarsaludables);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Regresar a la activity anterior
            }
        });

        // Configurar botón Enviar (opcional)
        Button btnEnviar = findViewById(R.id.btnEnviarHabito);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar lógica para enviar el hábito
                // Por ahora solo regresamos
                finish();
            }
        });
    }
}
