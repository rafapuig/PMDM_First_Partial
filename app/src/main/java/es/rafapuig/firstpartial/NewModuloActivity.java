package es.rafapuig.firstpartial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import es.rafapuig.firstpartial.databinding.ActivityNewModuloBinding;
import es.rafapuig.firstpartial.model.Ciclo;
import es.rafapuig.firstpartial.model.Curso;
import es.rafapuig.firstpartial.model.Modulo;
import es.rafapuig.firstpartial.persistence.FileDataAccessManager;
import es.rafapuig.firstpartial.persistence.ModuleFileDataAccessManager;

public class NewModuloActivity extends AppCompatActivity {

    FileDataAccessManager<Modulo> dataAccessManager;

    ActivityNewModuloBinding binding;

    SeekBar seekBarHours;
    TextView tvWeeklyHours;
    Spinner spinnerCiclos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_modulo);

        binding = ActivityNewModuloBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvWeeklyHours = findViewById(R.id.tvWeeklyHours);

        initSpinnerCiclos();

        initSeekBarWeeklyHours();

        setWeeklyHoursTextView(seekBarHours.getProgress());

        initSaveButton();

        initCancelButton();

        initRadioGroupCourseLevel();

        dataAccessManager = new ModuleFileDataAccessManager(this, MainActivity.FILENAME);
    }

    private void initRadioGroupCourseLevel() {
        //TODO: set the listener si that when radio button selected changes calls onRadioGroupCursoChanged callback method
        binding.rbgCourseLevel.setOnCheckedChangeListener(this::onRadioGroupCursoChanged);
    }

    Curso selectedCurso = Curso.PRIMERO;

    void onRadioGroupCursoChanged(RadioGroup group, int checkedId) {
        // TODO: set the selectedCurso variable depending on the checkedId argument value.
        if (checkedId == R.id.rbFirst) {
            selectedCurso = Curso.PRIMERO;
        } else {
            selectedCurso = Curso.SEGUNDO;
        }
    }


    private void initCancelButton() {
        //TODO set a listener for btnCancel that return RESULT_CANCELED at finish
        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            this.finish();
        });
    }

    private void initSaveButton() {
        //TODO set a listener for btnSave that save a Register and return RESULT_OK at finish
        //Init button Save
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v ->  {
            saveData();
            //Intent result = new Intent();
            setResult(RESULT_OK);
            finish();
        });
    }

    private void initSeekBarWeeklyHours() {
        //TODO set a listenet that updates the WeeklyHoursTextView dependind on the progress value
        seekBarHours = findViewById(R.id.seekBarWeekHours);

        seekBarHours.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setWeeklyHoursTextView(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    void setWeeklyHoursTextView(int hours) {
        tvWeeklyHours.setText(Integer.toString(hours));
    }


    Ciclo selectedCiclo = null;

    private void initSpinnerCiclos() {

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
                // TODO: set selectedCiclo from position parameter
                selectedCiclo = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    private void saveData() {
        // TODO: Get the module register info from UI widgets
        Ciclo ciclo =  selectedCiclo; // (Ciclo) spinnerCiclos.getSelectedItem();
        Curso curso = selectedCurso;
        String moduleName = binding.editModuleName.getText().toString();
        int weeklyHours = binding.seekBarWeekHours.getProgress();
        String teacherName = binding.editTeacherName.getText().toString();


        // TODO: Instantiate a new Modulo
        Modulo modulo = new Modulo(ciclo, curso, moduleName, weeklyHours, teacherName);

        //TODO : Write modulo to file (inside if condition
        if (dataAccessManager.writeRegisterDataToFile(modulo)) {
            showMessage("Module added!!!");
            clearUI();
        } else {
            showMessage("There was an error writing data!");
        }
    }

    void showMessage(String message) {
        //TODO: Show a toast with the message
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    void clearUI() {
        //TODO: clear the UI widgets values
        binding.seekBarWeekHours.setProgress(0);
        binding.editModuleName.setText("");
        binding.editTeacherName.setText("");
    }


}