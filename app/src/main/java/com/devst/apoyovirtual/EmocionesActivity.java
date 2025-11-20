package com.devst.apoyovirtual;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;import java.util.ArrayList;
import java.util.List;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import android.graphics.Color;
import android.content.DialogInterface;
import android.database.Cursor; // Solo si decides mantener SQLite un tiempo más
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter; // Necesario para el adaptador simple de Firebase
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmocionesActivity extends AppCompatActivity {

    private EditText editTextNombreEmocion;
    private EditText editTextDescripcionEmocion;

    // Unifiqué los botones. Usaremos 'btnAgregarEmocion' para guardar en Firebase
    private Button btnAgregarEmocion;
    private Button btnModificarEmocion;
    private Button btnEliminarEmocion;
    private ListView listViewEmociones;

    // Variables de SQLite (puedes borrarlas si ya no las usas)
    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private int selectedEmocionId = -1;

    private View viewColorPicker;
    private String selectedColorHex = "#FFFFFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emociones);

        // Inicialización
        dbHelper = new DatabaseHelper(this);

        editTextNombreEmocion = findViewById(R.id.editTextNombreEmocion);
        viewColorPicker = findViewById(R.id.viewColorPicker);
        editTextDescripcionEmocion = findViewById(R.id.editTextDescripcionEmocion);
        btnAgregarEmocion = findViewById(R.id.btnAgregarEmocion);
        btnModificarEmocion = findViewById(R.id.btnModificarEmocion);
        btnEliminarEmocion = findViewById(R.id.btnEliminarEmocion);
        listViewEmociones = findViewById(R.id.listViewEmociones);

        // CARGA DE DATOS: Elige UNO solo.
        // loadEmociones(); // <-- SQLite (Comentado para usar Firebase)
        cargarEmocionesDeFirebase(); // <-- Firebase (Activo)

        // --- BOTÓN AGREGAR (GUARDAR EN FIREBASE) ---
        btnAgregarEmocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí llamamos al método de Firebase en lugar del de SQLite
                guardarEmocionEnFirebase();
            }
        });

        // --- BOTÓN MODIFICAR (Aún con lógica SQLite, idealmente migrar a Firebase) ---
        btnModificarEmocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmocion();
            }
        });

        // --- BOTÓN ELIMINAR (Aún con lógica SQLite) ---
        btnEliminarEmocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmocion();
            }
        });

        // --- CLICK EN LA LISTA ---
        // Nota: Si usas Firebase, el adaptador cambia. Este listener es para SQLite.
        // Si usas el ArrayAdapter de Firebase, el objeto 'adapter.getItem(position)' será un String, no un Cursor.
        listViewEmociones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lógica temporal para verificar qué adaptador estamos usando
                Object item = parent.getItemAtPosition(position);

                if (item instanceof Cursor) {
                    // Lógica antigua (SQLite)
                    Cursor cursor = (Cursor) item;
                    selectedEmocionId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    editTextNombreEmocion.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMOCION_CATALOGO_NOMBRE)));
                    selectedColorHex = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMOCION_CATALOGO_COLOR));
                    viewColorPicker.setBackgroundColor(Color.parseColor(selectedColorHex));
                    editTextDescripcionEmocion.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMOCION_CATALOGO_DESCRIPCION)));
                } else {
                    // Lógica nueva (Firebase) - Por ahora solo muestra el nombre al hacer clic
                    String nombreEmocion = (String) item;
                    editTextNombreEmocion.setText(nombreEmocion);
                    Toast.makeText(EmocionesActivity.this, "Seleccionaste: " + nombreEmocion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });
    }

    // --- MÉTODOS DE FIREBASE ---

    private void guardarEmocionEnFirebase() {
        // 1. Obtener datos de la pantalla
        String nombre = editTextNombreEmocion.getText().toString().trim();
        String color = selectedColorHex;
        String descripcion = editTextDescripcionEmocion.getText().toString().trim();

        // 2. Validar
        if (nombre.isEmpty()) {
            Toast.makeText(this, "Escribe un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Crear objeto Emocion
        Emocion nuevaEmocion = new Emocion(nombre, color, descripcion);

        // 4. Conectar a Firebase y guardar
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("emociones")
                .add(nuevaEmocion)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "¡Guardado en Firebase!", Toast.LENGTH_SHORT).show();
                    clearFields(); // Corregido: Usamos clearFields() en lugar de limpiarCampos()
                    cargarEmocionesDeFirebase(); // Recargar la lista
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void cargarEmocionesDeFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> listaNombres = new ArrayList<>();

        db.collection("emociones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Emocion emocion = document.toObject(Emocion.class);
                            listaNombres.add(emocion.getNombre());
                        }

                        // Adaptador simple para mostrar los nombres traídos de Firebase
                        ArrayAdapter<String> adapterFirebase = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_list_item_1,
                                listaNombres
                        );
                        listViewEmociones.setAdapter(adapterFirebase);

                    } else {
                        Toast.makeText(this, "Error cargando lista", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // --- MÉTODOS DE UTILIDAD Y UI ---

    private void showColorPickerDialog() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Elige un color")
                .initialColor(Color.parseColor(selectedColorHex))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton("Aceptar", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        selectedColorHex = String.format("#%06X", (0xFFFFFF & selectedColor));
                        viewColorPicker.setBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .build()
                .show();
    }

    private void clearFields() {
        editTextNombreEmocion.setText("");
        selectedColorHex = "#FFFFFF";
        viewColorPicker.setBackgroundColor(Color.parseColor(selectedColorHex)); // Restaurar color visual
        editTextDescripcionEmocion.setText("");
        selectedEmocionId = -1;
    }

    // --- MÉTODOS ANTIGUOS (SQLITE) ---
    // Mantenlos si quieres, pero ya no se usan para agregar

    private void loadEmociones() {
        Cursor cursor = dbHelper.getAllEmociones();
        String[] from = new String[]{DatabaseHelper.COLUMN_EMOCION_CATALOGO_NOMBRE, DatabaseHelper.COLUMN_EMOCION_CATALOGO_DESCRIPCION};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, 0);
        listViewEmociones.setAdapter(adapter);
    }

    private void addEmocion() {
        // ESTE MÉTODO YA NO SE USA SI USAS FIREBASE
        // ... código antiguo ...
    }

    private void updateEmocion() {
        // ... código antiguo SQLite ...
        // Nota: Modificar en Firebase requiere obtener el ID del documento, no un int.
        Toast.makeText(this, "Modificar aún no implementado en Firebase", Toast.LENGTH_SHORT).show();
    }

    private void deleteEmocion() {
        // ... código antiguo SQLite ...
        Toast.makeText(this, "Eliminar aún no implementado en Firebase", Toast.LENGTH_SHORT).show();
    }
}
