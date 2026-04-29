package project.model;

public class Coach extends Person {

    private static int coachCount = 0;
    private int        coachId;
    private String     speciality;

    public Coach(String name, int age, String nationality, String speciality) {
        super(name, age, nationality);
        coachCount++;
        this.coachId = coachCount;
        this.speciality = speciality;
    }

    // ---- Getters & Setters -------------------------------------------------------------
    public int getCoachId()       { return coachId; }
    public String getSpeciality() { return speciality; }

    public void setSpeciality(String speciality) { this.speciality = speciality; }

    @Override
    public void displayInfo() {
        System.out.println("_".repeat(30));
        System.out.println("Coach ID    : " + coachId);
        System.out.println("Name        : " + getName());
        System.out.println("Age         : " + getAge());
        System.out.println("Nationality : " + getNationality());
        System.out.println("Specialty   : " + getSpeciality());
    }
}
