package es.rafapuig.firstpartial.persistence;

import android.content.Context;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.EnumSet;

import es.rafapuig.firstpartial.model.Ciclo;
import es.rafapuig.firstpartial.model.Curso;
import es.rafapuig.firstpartial.model.Modulo;

public class ModuleFileDataAccessManager extends FileDataAccessManager<Modulo> {

    public ModuleFileDataAccessManager(Context context, String filename) {
        super(context, filename);
    }

    @Override
    void writeRegister(DataOutputStream dos, Modulo modulo) throws IOException {
        dos.writeInt(modulo.getCiclo().ordinal());
        dos.writeInt(modulo.getCurso().ordinal());
        //TODO: Write the remaining fields

    }

    @Override
    Modulo readRegister(DataInputStream dis) throws IOException {
        int cicloOrdinal = dis.readInt();
        Ciclo ciclo = EnumSet.allOf(Ciclo.class).stream().filter(ciclo1 -> ciclo1.ordinal() == cicloOrdinal).findFirst().get();

        int cursoOrdinal = dis.readInt();
        Curso curso = EnumSet.allOf(Curso.class).stream().filter(curso1 -> curso1.ordinal() == cursoOrdinal).findFirst().get();

        //TODO: read the reamining fields


        //TODO: instantiate a new Modulo object with the read values and return it

    }
}
