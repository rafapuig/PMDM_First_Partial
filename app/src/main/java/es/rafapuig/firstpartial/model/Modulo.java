package es.rafapuig.firstpartial.model;

public class Modulo {
    private Ciclo ciclo;
    private Curso curso;
    private String name;
    private int weeklyHours;
    private String teacherName;

    public Modulo(Ciclo ciclo, Curso curso, String name, int weeklyHours, String teacherName) {
        this.ciclo = ciclo;
        this.curso = curso;
        this.name = name;
        this.weeklyHours = weeklyHours;
        this.teacherName = teacherName;
    }

    public Ciclo getCiclo() {
        return ciclo;
    }

    public Curso getCurso() {
        return curso;
    }

    public String getName() {
        return name;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    public String getTeacherName() {
        return teacherName;
    }
}
