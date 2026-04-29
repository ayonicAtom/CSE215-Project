package project.model;

public abstract class Person {

    private String name;
    private int    age;
    private String nationality;
    private double height;

    public Person(String name, int age, double height, String nationality ) {
        this.name = name;
        this.age = age;
        this.nationality = nationality;
        this.height = height;
    }

    public Person(String name, int age, String nationality) {
        this.name = name;
        this.age = age;
        this.nationality = nationality;
    }

    // ---- Getters & Setters -------------------------------------------------------------
    public String getName()        { return name; }
    public int getAge()            { return age; }
    public String getNationality() { return nationality; }
    public double getHeight()      { return height; }

    public void setName(String name)               { this.name = name; }
    public void setAge(int age)                    { this.age = age; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public void setHeight(double height)           { this.height = height; }

    public abstract void displayInfo();
}
