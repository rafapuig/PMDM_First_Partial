package es.rafapuig.firstpartial.model;

public enum Ciclo {

    DAM ("Desarrollo de Aplicaciones Multimedia"),
    DAW ("Desarrollo de Aplicaciones Web"),
    ASIR ("Administracion de Sistemas Informaticos en Red");

    private final String name;

    public String getName() {
        return name;
    }

    Ciclo(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
