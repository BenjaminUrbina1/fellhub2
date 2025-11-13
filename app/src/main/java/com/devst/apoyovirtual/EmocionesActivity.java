package com.devst.apoyovirtual;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import android.graphics.Color; // ¡Importante que sea esta!
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmocionesActivity extends AppCompatActivity {

    private EditText editTextNombreEmocion;
    private EditText editTextDescripcionEmocion;
    private Button btnAgregarEmocion;
    private Button btnModificarEmocion;
    private Button btnEliminarEmocion;
    private ListView listViewEmociones;

    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private int selectedEmocionId = -1;

    private View viewColorPicker;
    private String selectedColorHex = "#FFFFFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emociones);

        dbHelper = new DatabaseHelper(this);

        editTextNombreEmocion = findViewById(R.id.editTextNombreEmocion);
        viewColorPicker = findViewById(R.id.viewColorPicker);
        editTextDescripcionEmocion = findViewById(R.id.editTextDescripcionEmocion);
        btnAgregarEmocion = findViewById(R.id.btnAgregarEmocion);
        btnModificarEmocion = findViewById(R.id.btnModificarEmocion);
        btnEliminarEmocion = findViewById(R.id.btnEliminarEmocion);
        listViewEmociones = findViewById(R.id.listViewEmociones);

        loadEmociones();

        btnAgregarEmocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmocion();
            }
        });

        btnModificarEmocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmocion();
            }
        });

        btnEliminarEmocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmocion();
            }
        });

        listViewEmociones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) adapter.getItem(position);
                selectedEmocionId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                editTextNombreEmocion.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMOCION_CATALOGO_NOMBRE)));
                selectedColorHex = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMOCION_CATALOGO_COLOR));
                viewColorPicker.setBackgroundColor(Color.parseColor(selectedColorHex));
                editTextDescripcionEmocion.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMOCION_CATALOGO_DESCRIPCION)));
            }
        });

        viewColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });
    }

    private void showColorPickerDialog() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Elige un color")
                .initialColor(Color.parseColor(selectedColorHex))

                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        // Este listener es opcional, se llama mientras mueves el selector
                    }
                })
                .setPositiveButton("Aceptar", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        // Guardamos el color seleccionado y actualizamos la vista
                        selectedColorHex = String.format("#%06X", (0xFFFFFF & selectedColor));
                        viewColorPicker.setBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void loadEmociones() {
        Cursor cursor = dbHelper.getAllEmociones();

        String[] from = new String[]{DatabaseHelper.COLUMN_EMOCION_CATALOGO_NOMBRE, DatabaseHelper.COLUMN_EMOCION_CATALOGO_DESCRIPCION};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, 0);
        listViewEmociones.setAdapter(adapter);
    }

    private void addEmocion() {
        String nombre = editTextNombreEmocion.getText().toString().trim();
        String color = selectedColorHex;
        String descripcion = editTextDescripcionEmocion.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor, completa el nombre y el color", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = dbHelper.addEmocion(nombre, color, descripcion);

        if (result != -1) {
            Toast.makeText(this, "Emoción agregada", Toast.LENGTH_SHORT).show();
            clearFields();
            loadEmociones();
        } else {
            Toast.makeText(this, "Error al agregar la emoción", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmocion() {
        if (selectedEmocionId == -1) {
            Toast.makeText(this, "Selecciona una emoción para modificar", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = editTextNombreEmocion.getText().toString().trim();
        String color = selectedColorHex;
        String descripcion = editTextDescripcionEmocion.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor, completa el nombre y el color", Toast.LENGTH_SHORT).show();
            return;
        }

        int result = dbHelper.updateEmocion(selectedEmocionId, nombre, color, descripcion);

        if (result > 0) {
            Toast.makeText(this, "Emoción modificada", Toast.LENGTH_SHORT).show();
            clearFields();
            loadEmociones();
        } else {
            Toast.makeText(this, "Error al modificar la emoción", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteEmocion() {
        if (selectedEmocionId == -1) {
            Toast.makeText(this, "Selecciona una emoción para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        int result = dbHelper.deleteEmocion(selectedEmocionId);

        if (result > 0) {
            Toast.makeText(this, "Emoción eliminada", Toast.LENGTH_SHORT).show();
            clearFields();
            loadEmociones();
        } else {
            Toast.makeText(this, "Error al eliminar la emoción", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        editTextNombreEmocion.setText("");
        selectedColorHex = "#FFFFFF";
        editTextDescripcionEmocion.setText("");
        selectedEmocionId = -1;
    }
}
