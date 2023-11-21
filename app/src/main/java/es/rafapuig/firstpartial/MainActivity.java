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

    }


    private void loadCurrentRegisterPositionPreference() {
        // TODO: retrieve the last position pointed to a register of the collection
        // TODO: if not is present then use position 0

    }

    private void initButtonNew() {
        //TODO: set a Click listener for the button btnNew that starts the NewModuloActivity for result

    }

    //TODO: Create the ActivityResultLauncher
    //TODO: set onActivityResult callback method initialModuleIndex = collection Size if RESULT_OK




    @Override
    protected void onPause() {
        initialModuleIndex = currentModuleIndex;

        // TODO: other things to do when activity enters on pause

        super.onPause();
    }

    private void saveCurrentRegisterPositionPreference() {
        //TODO: Save the current register position in app preferences

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            //TODO: load data from data storage

            //TODO: initialize current register index

            //TODO: Show current register in the UI


            throw new IOException("REMOVE THIS SENTENCE"); // TODO: remove because is here only for avoiding compilation errors temporally
        } catch (IOException e) {
            //TODO: Show a message with the error using a Toast

        }
    }

    void initRegisterIndex() {
        // TODO: Point to the register in the collection we will show initially

    }

    Modulo getModuleFromIndex(int index) {
        // TODO: return a reference to the register located in the provided index,
        // TODO: if index is out of bounds return null


        return null; //TODO: delete this because is here only to remove compilation error temporally
    }

    void showCurrentRegister() {
        // TODO: Show in the UI data from the current register by currentModuleIndex

    }

    public void onPreviousButtonClickCallback(View view) {
        // TODO: Do currentModuleIndex points to previous position in the list and show data
        // TODO: If you are already in the first position show a Toast message

    }

    public void onNextButtonClickCallback(View view) {
        // TODO: Do currentModuleIndex points to next position in the list and show data
        // TODO: If you are already in the last position show a Toast message

    }


    public void onFirstButtonClickCallback(View view) {
        // TODO: Do currentModuleIndex points to first position of the list and show data
        // TODO: If you are already in the first position show a Toast message

    }

    public void onLastButtonClickCallback(View view) {
        // TODO: Do currentModuleIndex points to last position of the list and show data
        // TODO: If you are already in the last position show a Toast message

    }
}