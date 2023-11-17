package es.rafapuig.firstpartial.persistence;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

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
import es.rafapuig.firstpartial.model.Curso;
import es.rafapuig.firstpartial.model.Modulo;

public abstract class FileDataAccessManager<T> {

    private final String filename;

    private boolean isWriteableSD; // Is there an SD card in read/write mode?

    // In modern versions of Android, 1st SD is always emulated in an internal memory folder
    // So, the first real SD card is the second of the array
    private File[] sdCards; //SD cards available array

    Context context;

    public FileDataAccessManager(Context context, String filename) {
        this.filename = filename;
        this.context = context;
        manageExternalStorage();
    }

    void manageExternalStorage() {
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


    public boolean writeRegisterDataToFile(T register) {
        try {
            tryToWriteDataToFile(register);
            return true;
        } catch (IOException e) {
            Log.i("", "There was an error writing to the file!" + e.getMessage());
        }
        return false;
    }

    void tryToWriteDataToFile(T register) throws IOException {
        FileOutputStream fos = getFileOutputStream(filename, true); // byte stream to a file
        // We use APPEND mode so that new data is added to an existent file with all historical saved data
        BufferedOutputStream bos = new BufferedOutputStream(fos); // Optimize bytes to blocks writings
        DataOutputStream dos = new DataOutputStream(bos); // translate typed data to bytes

        // Write register info in the file as 3 typed data
        writeRegister(dos, register);

        // We must close the output streams
        dos.close();
        bos.close();
        fos.close();
    }

    abstract void writeRegister(DataOutputStream dos, T register) throws IOException;


    private FileInputStream getFileInputStream(String filename) throws FileNotFoundException {
        if (isWriteableSD) // There is an SD
            return new FileInputStream(getSDCardFile(filename));
        else
            return context.openFileInput(filename); // If there's no SD, use internal storage
    }


    List<T> registers = new ArrayList<>();

    public List<T> getAllRegisters() {
        return registers;
    }


    void readRegisters(DataInputStream input) throws IOException {

        this.registers.clear();

        while (true) {
            T register = readRegister(input);
            this.registers.add(register);
        }
    }

    abstract T readRegister(DataInputStream dis) throws IOException;


    public void loadData() throws IOException {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        try {
            // read from file each register, get the fields that make up a Person object and process the data

            fis = getFileInputStream(filename);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            readRegisters(dis);

        } catch (EOFException e) //We have ended the reading of data in the file
        {
            try {
                // close all input streams!
                dis.close();
                bis.close();
                fis.close();

            } catch (IOException ioe) {
                throw new IOException("There was an error closing the streams!: ", ioe);
            }
        } catch (FileNotFoundException fnfe) {
            throw new IOException("STILL NO DATA", fnfe); // File not exists!

        } catch (IOException ioe) {
            throw new IOException("Error reading the file!: ", ioe);
        }
    }


}
