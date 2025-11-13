package com.devst.apoyovirtual;

import android.os.Bundle;
import android.widget.Button;import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

// 1. La clase debe heredar de AppCompatActivity para funcionar como una pantalla.
public class CrearCuentaActivity extends AppCompatActivity {

    // 2. Declarar las variables para los componentes del layout y el helper de la BD.
    private TextInputEditText editTextUsername, editTextEmail, editTextPassword;
    private Button registerButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 3. Enlazar esta Activity con su archivo de diseño XML 'crearc_uenta'.
        setContentView(R.layout.crearc_uenta);

        // 4. Inicializar el DatabaseHelper.
        dbHelper = new DatabaseHelper(this);

        // 5. Vincular las variables con los componentes del XML usando sus IDs.
        //    Esto funcionará si el XML 'crearc_uenta' tiene estos IDs definidos.
        editTextUsername = findViewById(R.id.editTextRegisterUsername);
        editTextEmail = findViewById(R.id.editTextRegisterEmail);
        editTextPassword = findViewById(R.id.editTextRegisterPassword);
        registerButton = findViewById(R.id.registerButton);

        // 6. Configurar el listener para que el botón "escuche" los clics.
        registerButton.setOnClickListener(v -> {
            // Llama a la función que maneja la lógica del registro.
            handleRegistration();
        });
    }

    /**
     * Función para gestionar el proceso de registro de un nuevo usuario.
     */
    private void handleRegistration() {
        // 7. Obtener los datos de los campos de texto y limpiar espacios en blanco.
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // 8. Validar que los campos no estén vacíos.
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return; // Detiene la ejecución si algún campo está vacío.
        }

        // 9. Llamar al método 'addUser' del DatabaseHelper para insertar el usuario.
        long result = dbHelper.addUser(username, email, password);

        // 10. Verificar el resultado de la inserción en la base de datos.
        if (result != -1) {
            // Si el resultado es diferente de -1, el usuario se insertó correctamente.
            Toast.makeText(this, "Registro exitoso. Ahora puedes iniciar sesión.", Toast.LENGTH_LONG).show();
            finish(); // Cierra esta Activity y regresa a la anterior (MainActivity).
        } else {
            // Si el resultado es -1, ocurrió un error.
            Toast.makeText(this, "Error en el registro. El email podría estar ya en uso.", Toast.LENGTH_LONG).show();
        }
    }
}
