package com.devst.apoyovirtual;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    // 1. Declaramos las variables con nombres más claros
    private TextInputEditText editTextUsername, editTextPassword; // Cambiado de editTextEmail a editTextUsername
    private Button loginButton;
    private TextView createAccountLink;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        // 3. Vinculación correcta (como la tenías)
        editTextUsername = findViewById(R.id.editTextUsername); // ¡Esto ahora es correcto para tu lógica!
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);
        createAccountLink = findViewById(R.id.createAccountLink);

        // 4. Listener del botón de login
        loginButton.setOnClickListener(v -> {
            handleLogin();
        });

        // 5. Listener para crear cuenta
        createAccountLink.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CrearCuentaActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        // 6. Obtener el nombre de usuario y contraseña
        String username = editTextUsername.getText().toString().trim(); // La variable ahora tiene un nombre más lógico
        String password = editTextPassword.getText().toString().trim();

        // 7. Validar que los campos no estén vacíos
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // 8. Llamar al método checkUser (que ahora busca por nombre de usuario)
        boolean isValidUser = dbHelper.checkUser(username, password);

        // 9. Comprobar el resultado
        if (isValidUser) {
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, principalActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}
