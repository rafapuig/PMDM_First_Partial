package es.rafapuig.firstpartial;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import es.rafapuig.firstpartial.databinding.ActivityMainBinding;
import es.rafapuig.firstpartial.model.Modulo;
import es.rafapuig.firstpartial.persistence.FileDataAccessManager;
import es.rafapuig.firstpartial.persistence.ModuleFileDataAccessManager;

public class MainActivity extends AppCompatActivity {

    public static final String FILENAME = "modulos.dat";

    public static final String INITIAL_REGISTER_POSITION = "initial_register_position";

    ActivityMainBinding binding;

    FileDataAccessManager<Modulo> dataAccessManager;
    private int currentModuleIndex = -1;

    private int initialModuleIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dataAccessManager = new ModuleFileDataAccessManager(this, FILENAME);

        initButtonNew();

        //TODO: other things to do when the activity is created...
        loadCurrentRegisterPositionPreference();
    }


    private void loadCurrentRegisterPositionPreference() {
        // TODO: retrieve the last position pointed to a register of the collection
        // TODO: if not is present then use position 0
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        initialModuleIndex = sharedPreferences.getInt(INITIAL_REGISTER_POSITION, 0);
    }

    private void initButtonNew() {
        //TODO: set a Click listener for the button btnNew that starts the NewModuloActivity for result
        binding.btnNew.setOnClickListener(view ->
        {
            Intent newModulo = new Intent(this, NewModuloActivity.class);
            //startActivity(newModulo);
            startNewModuloActivityForResult.launch(newModulo);
        });
    }

    //TODO: Create the ActivityResultLauncher
    //TODO: set onActivityResult callback method lastModulePosition = collectionSize if RESULT_OK

    ActivityResultLauncher<Intent> startNewModuloActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        initialModuleIndex = dataAccessManager.getAllRegisters().size();
                        Log.i("onActivityResult",Integer.toString(initialModuleIndex));
                    }
                }
            }
    );


    @Override
    protected void onPause() {
        initialModuleIndex = currentModuleIndex;

        // TODO: other things to do when activity enters on pause
        saveCurrentRegisterPositionPreference();
        super.onPause();
    }

    private void saveCurrentRegisterPositionPreference() {
        //TODO: Save the current register position in app preferences
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(INITIAL_REGISTER_POSITION, currentModuleIndex);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            //TODO: load data from data storage
            dataAccessManager.loadData();
            //TODO: initialize current register index
            initRegisterIndex();
            //TODO: Show current register in the UI
            showCurrentRegister();

        } catch (IOException e) {
            //TODO: Show a message with the error using a Toast
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void initRegisterIndex() {
        // TODO: Point to the first register in the collection
        if (!dataAccessManager.getAllRegisters().isEmpty()) currentModuleIndex = initialModuleIndex;
        else currentModuleIndex = -1;
    }

    Modulo getModuleFromIndex(int index) {
        // TODO: return a reference to the register located in the provided index,
        // TODO: if index is out of bounds return null
        try {
            return dataAccessManager.getAllRegisters().get(index);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    void showCurrentRegister() {
        // TODO: Show in the UI data form the current register by currentModuleIndex
        Modulo modulo = getModuleFromIndex(currentModuleIndex);

        if (modulo != null) {
            binding.tvStudies.setText(modulo.getCiclo().getName());

            binding.rbFirst.setChecked(modulo.getCurso().getNumeral() == 1);
            binding.rbSecond.setChecked(modulo.getCurso().getNumeral() == 2);

            binding.tvWeeklyHours.setText(Integer.toString(modulo.getWeeklyHours()));
            binding.tvModuleName.setText(modulo.getName());
            binding.tvTeacher.setText(modulo.getTeacherName());
        }
    }

    public void onPreviousButtonClickCallback(View view) {
        // TODO: Do currentModuleIndex points to previous position in the list and show data
        // TODO: If you are already in the first position show a Toast message
        if (currentModuleIndex > 0) {
            currentModuleIndex--;
            showCurrentRegister();
        } else {
            Toast.makeText(this, "There's no previous register", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNextButtonClickCallback(View view) {
        // TODO: Do currentModuleIndex points to next position in the list and show data
        // TODO: If you are already in the last position show a Toast message
        if (currentModuleIndex < dataAccessManager.getAllRegisters().size() - 1) {

            currentModuleIndex++;
            showCurrentRegister();
        } else {
            Toast.makeText(this, "There's no next register", Toast.LENGTH_SHORT).show();
        }
    }


    public void onFirstButtonClickCallback(View view) {
        // TODO: Do currentModuleIndex points to first position of the list and show data
        // TODO: If you are already in the first position show a Toast message
        if (currentModuleIndex != 0) {
            currentModuleIndex = 0;
            showCurrentRegister();
        } else {
            Toast.makeText(this, "This is the first register", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLastButtonClickCallback(View view) {
        // TODO: Do currentModuleIndex points to last position of the list and show data
        // TODO: If you are already in the last position show a Toast message
        int registersCount = dataAccessManager.getAllRegisters().size();

        if (currentModuleIndex != registersCount - 1) {
            currentModuleIndex = registersCount - 1;
            showCurrentRegister();
        } else {
            Toast.makeText(this, "This is the last register", Toast.LENGTH_SHORT).show();
        }
    }
}