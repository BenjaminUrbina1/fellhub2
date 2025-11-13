package com.devst.apoyovirtual;

// IMPORTACIONES NECESARIAS
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class quienessomosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quienessomos); // Asegúrate de que este layout existe

        // Configurar botón Regresar
        setupBackButton();
    }

    private void setupBackButton() {
        // Buscar el botón por ID (debes tener un botón con este ID en tu XML)
        Button btnRegresar = findViewById(R.id.btnRegresarquienessomos);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Método 1: Cierra esta activity y regresa a la anterior
                finish();

                // Método 2: Si quieres regresar a PrincipalActivity específicamente
                // Intent intent = new Intent(QuienesSomosActivity.this, PrincipalActivity.class);
                // startActivity(intent);
                // finish(); // Cierra la activity actual
            }
        });
    }
}