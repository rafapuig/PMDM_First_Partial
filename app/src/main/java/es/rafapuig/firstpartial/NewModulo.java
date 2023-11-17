package es.rafapuig.firstpartial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.rafapuig.firstpartial.model.Ciclo;
import es.rafapuig.firstpartial.model.Curso;
import es.rafapuig.firstpartial.model.Modulo;
import es.rafapuig.firstpartial.persistence.DataAccessManager;

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

        spinnerCiclos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Ciclo> adapter = (ArrayAdapter<Ciclo>) parent.getAdapter();
                Ciclo ciclo = adapter.getItem(position);
                TextView  tvLog  = findViewById(R.id.tvLog);
                tvLog.setText(ciclo.name());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveData());


    }

    void setWeeklyHoursTextView(int hours) {
        tvWeeklyHours.setText(Integer.toString(hours));
    }

    private void saveData() {
        // Get the register info

        DataAccessManager dam = new DataAccessManager(this);

        Modulo modulo = new Modulo(Ciclo.ASIR, Curso.SEGUNDO, "Servicios de Red e Internet", 6, "Ramon Arnal");


       /* String fullName = binding.fullNameEditText.getText().toString();
        if(!validateNameField(fullName)) return;

        String ageFieldContent = binding.ageEditText.getText().toString();
        if(!validateAgeField(ageFieldContent)) return;
        int age = Integer.parseInt(ageFieldContent);

        boolean isWorking = binding.workingCheckbox.isChecked();

        */


        //Write data to file
        if(dam.writeDataToFile(modulo)) {
            clearUI();
        } else {
            showError("There was an error writing data!");
        }
    }

    void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    void clearUI() {
        /*binding.fullNameEditText.setText("");
        binding.ageEditText.setText("");
        binding.workingCheckbox.setChecked(false);*/
    }


}