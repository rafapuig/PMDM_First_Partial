package es.rafapuig.firstpartial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import es.rafapuig.firstpartial.model.Ciclo;

public class NewModulo extends AppCompatActivity {

    SeekBar seekBarHours;
    TextView tvWeeklyHours;
    Spinner spinnerCiclos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_modulo);

        seekBarHours = findViewById(R.id.seekBarWeekHours);
        tvWeeklyHours = findViewById(R.id.tvWeeklyHours);

        setWeeklyHoursTextView(seekBarHours.getProgress());

        seekBarHours.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setWeeklyHoursTextView(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        spinnerCiclos = findViewById(R.id.spinnerCiclos);

        ArrayAdapter<Ciclo> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new Ciclo[]{Ciclo.ASIR, Ciclo.DAM, Ciclo.DAW});

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCiclos.setAdapter(adapter);


    }

    void setWeeklyHoursTextView(int hours) {
        tvWeeklyHours.setText(Integer.toString(hours));
    }
}