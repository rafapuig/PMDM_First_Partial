package es.rafapuig.firstpartial.persistence;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import es.rafapuig.firstpartial.model.Ciclo;
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



    private FileInputStream getFileInputStream(String filename) throws FileNotFoundException
    {
        if (isWriteableSD) // There is an SD
            return new FileInputStream(getSDCardFile(filename));
        else
            return context.openFileInput(filename); // If there's no SD, use internal storage
    }

    void processRegisterData(String fullName, int age, boolean isWorking) {
        // TODO: Create a new Person with the method parameters and add to the data structure
        Person person = new Person(fullName, age, isWorking);
        addPerson(person);
    }

    void readModulo(DataInputStream dis) throws IOException {

        int cicloOrdinal = dis.readInt();

        Ciclo ciclo = EnumSet.allOf(Ciclo.class).stream().filter(ciclo1 -> ciclo1.ordinal() == cicloOrdinal).findFirst().get();



    }

    List<Modulo> modulos = new ArrayList<>();

    private void loadPeopleData() throws IOException {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        try {
            // TODO: read from file each register, get the fields that make up a Person object and process the data

            fis = getFileInputStream(FILENAME);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            modulos.clear();

            while(true) {

                readModulo(dis);

                //String fullName = dis.readUTF();
                //int age = dis.readInt();
                //boolean isWorking = dis.readBoolean();

                //processRegisterData(fullName, age, isWorking);
            }

        } catch (EOFException e) //We have ended the reading of data in the file
        {
            try {
                // TODO: close all input streams!
                dis.close();
                bis.close();
                fis.close();

            } catch (IOException ioe)
            {
                throw new IOException("There was an error closing the streams!: ", ioe);
                /*Toast.makeText(this,"There was an error closing the streams!: " +
                        ioe.getMessage(), Toast.LENGTH_LONG).show();*/
            }
        }
        catch (FileNotFoundException fnfe)
        {
            throw new IOException("STILL NO DATA", fnfe);
            // File not exists!
            //Toast.makeText(this,"STILL NO DATA", Toast.LENGTH_LONG).show();
        }
        catch (IOException ioe)
        {
            throw new IOException("Error reading the file!: ", ioe);
            /*Toast.makeText(this,"Error reading the file!: " +
                    ioe.getMessage(), Toast.LENGTH_LONG).show();*/
        }
    }





}
