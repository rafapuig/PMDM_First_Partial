package es.rafapuig.firstpartial.model;

public enum Curso {

    PRIMERO(1),
    SEGUNDO(2);

    private int numeral;

    public int getNumeral() {
        return numeral;
    }

    Curso(int numeral) {
        this.numeral = numeral;
    }

}
