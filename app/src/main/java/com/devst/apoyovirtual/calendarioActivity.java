package com.devst.apoyovirtual;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class calendarioActivity extends AppCompatActivity {

    private GridLayout gridLayoutCalendarDays;
    private TextView tvMonthYear;
    private Calendar currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);

        gridLayoutCalendarDays = findViewById(R.id.gridLayoutCalendarDays);
        tvMonthYear = findViewById(R.id.tvMonthYear);
        currentCalendar = Calendar.getInstance();

        updateMonthYearText();
        populateCalendarDays();
    }

    private void updateMonthYearText() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        tvMonthYear.setText(sdf.format(currentCalendar.getTime()));
    }

    private void populateCalendarDays() {
        gridLayoutCalendarDays.removeAllViews();

        gridLayoutCalendarDays.setColumnCount(7);
        gridLayoutCalendarDays.setRowCount(6);

        Calendar tempCalendar = (Calendar) currentCalendar.clone();
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK);
        int firstDayOfMonthOffset = adjustDayOfWeek(firstDayOfWeek);
        int daysInMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int padding = 8;
        int cellSize = (screenWidth - (padding * 2)) / 7;

        for (int i = 0; i < firstDayOfMonthOffset; i++) {
            addEmptyCell(cellSize);
        }

        for (int day = 1; day <= daysInMonth; day++) {
            addDayCell(day, cellSize);
        }

        int totalCells = firstDayOfMonthOffset + daysInMonth;
        int remainingCells = 42 - totalCells;

        for (int i = 0; i < remainingCells; i++) {
            addEmptyCell(cellSize);
        }
    }

    private int adjustDayOfWeek(int calendarDayOfWeek) {
        int adjusted = calendarDayOfWeek - 2;
        if (adjusted < 0) {
            adjusted = 6;
        }
        return adjusted;
    }

    private void addEmptyCell(int cellSize) {
        TextView emptyCell = new TextView(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = cellSize;
        params.height = cellSize;
        params.setMargins(1, 1, 1, 1);

        emptyCell.setLayoutParams(params);
        emptyCell.setText("");
        emptyCell.setBackgroundColor(Color.TRANSPARENT);
        emptyCell.setGravity(Gravity.CENTER);

        gridLayoutCalendarDays.addView(emptyCell);
    }

    private void addDayCell(int day, int cellSize) {
        TextView dayCell = new TextView(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = cellSize;
        params.height = cellSize;
        params.setMargins(1, 1, 1, 1);

        dayCell.setLayoutParams(params);
        dayCell.setText(String.valueOf(day));
        dayCell.setGravity(Gravity.CENTER);
        dayCell.setTextSize(14);
        dayCell.setTextColor(Color.BLACK);

        dayCell.setBackgroundColor(Color.WHITE);
        dayCell.setBackgroundResource(R.drawable.calendar_day_border);

        applySpecialDayStyle(dayCell, day);
        highlightCurrentDay(dayCell, day);

        dayCell.setOnClickListener(v -> {
            System.out.println("Día seleccionado: " + day);
        });

        gridLayoutCalendarDays.addView(dayCell);
    }

    private void applySpecialDayStyle(TextView dayCell, int day) {
        if (day == 1) {
            dayCell.setBackgroundColor(ContextCompat.getColor(this, R.color.celeste_dia_1));
            dayCell.setTextColor(Color.WHITE);
        } else if (day == 12) {
            dayCell.setBackgroundColor(ContextCompat.getColor(this, R.color.rojo_claro_dia_12));
            dayCell.setTextColor(Color.WHITE);
        } else if (day == 16) {
            dayCell.setBackgroundColor(ContextCompat.getColor(this, R.color.amarillo_dia_16));
            dayCell.setTextColor(Color.BLACK);
        } else if (day == 22) {
            dayCell.setBackgroundColor(ContextCompat.getColor(this, R.color.naranjo_dia_22));
            dayCell.setTextColor(Color.BLACK);
        }
    }

    private void highlightCurrentDay(TextView dayCell, int day) {
        Calendar today = Calendar.getInstance();
        if (currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                day == today.get(Calendar.DAY_OF_MONTH)) {

            dayCell.setBackgroundColor(ContextCompat.getColor(this, R.color.day_background_selected));
            dayCell.setTextColor(Color.WHITE);
        }
    }

    public void onPreviousMonthClick(View view) {
        currentCalendar.add(Calendar.MONTH, -1);
        updateMonthYearText();
        populateCalendarDays();
    }

    public void onNextMonthClick(View view) {
        currentCalendar.add(Calendar.MONTH, 1);
        updateMonthYearText();
        populateCalendarDays();
    }

    // Nuevo método para el botón Regresar
    public void onRegresarClick(View view) {
        System.out.println("Botón Regresar clickeado");
        // Aquí puedes agregar la lógica para regresar a la actividad anterior
        finish(); // Cierra la actividad actual y regresa a la anterior
    }

    // Nuevo método para el botón Analizar
    public void onAnalizarClick(View view) {
        System.out.println("Botón Analizar clickeado");
        // Aquí puedes agregar la lógica para analizar las emociones
        // Por ejemplo, mostrar un diálogo, cambiar los mensajes, etc.
    }
}