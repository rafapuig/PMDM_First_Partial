package es.rafapuig.firstpartial.persistence;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import es.rafapuig.firstpartial.model.Modulo;

public class DataAccessManager {
    public static final String FILENAME = "modulos.dat";
    public boolean isWriteableSD; // Is there an SD card in read/write mode?

    // In modern versions of Android, 1st SD is always emulated in an internal memory folder
    // So, the first real SD card is the second of the array
    public File[] sdCards; //SD cards available array

    Context context;

    public DataAccessManager(Context context) {
        this.context = context;
        manageExternalStorage();
    }

    public File getSDCardFile(String filename) {
        File pathToSD = sdCards[1]; // The second is the real first one
        File file = new File(pathToSD.getAbsolutePath(), filename); // parent folder + filename
        return file;
    }

    private FileOutputStream getFileOutputStream(String filename, boolean append) throws FileNotFoundException {
        if (isWriteableSD) // There is an SD card physical
            return new FileOutputStream(getSDCardFile(filename), append); // 2nd parameter = append
        else
            // If there's no SD card, then use internal memory
            return context.openFileOutput(filename, append ? Context.MODE_APPEND : Context.MODE_PRIVATE);
    }

    public void manageExternalStorage() {
        // Check SD exists and we have read/write permissions
        String stateSD = Environment.getExternalStorageState();
        isWriteableSD = stateSD.equals(Environment.MEDIA_MOUNTED);

        // Now, check that it is not the emulated
        if (isWriteableSD) {
            // Get SD cards folders paths
            sdCards = ContextCompat.getExternalFilesDirs(context, null);
            // There really is a physical SD card if there are more than one items in the array
            isWriteableSD = isWriteableSD && (sdCards.length > 1);
        }
    }

    /*private void saveData() {
        // Get the register info

        String fullName = binding.fullNameEditText.getText().toString();
        if(!validateNameField(fullName)) return;

        String ageFieldContent = binding.ageEditText.getText().toString();
        if(!validateAgeField(ageFieldContent)) return;
        int age = Integer.parseInt(ageFieldContent);

        boolean isWorking = binding.workingCheckbox.isChecked();

        //Write data to file
        if(writeDataToFile(fullName, age, isWorking)) {
            clearUI();
        } else {
            showError("There was an error writing data!");
        }
    }*/

    public boolean writeDataToFile(Modulo modulo) {
        try {
            tryToWriteDataToFile(modulo);
            return true;
        } catch (IOException e) {
            Log.i("","There was an error writing to the file!" + e.getMessage());
        }
        return false;
    }

    void tryToWriteDataToFile(Modulo modulo) throws IOException {
        FileOutputStream fos = getFileOutputStream(FILENAME, true); // byte stream to a file
        // We use APPEND mode so that new data is added to an existent file with all historical saved data
        BufferedOutputStream bos = new BufferedOutputStream(fos); // Optimize bytes to blocks writings
        DataOutputStream dos = new DataOutputStream(bos); // translate typed data to bytes

        // Write register info in the file as 3 typed data
        writeModulo(dos, modulo);

        // We must close the output streams
        dos.close();
        bos.close();
        fos.close();
    }

    void writeModulo(DataOutputStream dos, Modulo modulo) throws IOException {
        dos.writeInt(modulo.getCiclo().ordinal());
        dos.writeInt(modulo.getCurso().ordinal());
        dos.writeUTF(modulo.getName());
        dos.writeInt(modulo.getWeeklyHours());
        dos.writeUTF(modulo.getTeacherName());
    }



}
