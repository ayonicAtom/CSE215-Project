package project.service;

import project.model.Coach;
import java.util.ArrayList;

public class CoachService {

    private ArrayList<Coach> coaches = new ArrayList<>();

    public void add(Coach coach) {
        coaches.add(coach);
    }

    public void delete(int id) {
        coaches.removeIf(c -> c.getCoachId() == id);
    }

    public Coach findById(int id) {
        for (Coach c : coaches) {
            if (c.getCoachId() == id) {
                return c;
            }
        }

        return null;
    }

    public boolean isEmpty() {
        return coaches.isEmpty();
    }

    public ArrayList<Coach> getCoaches() {
        return coaches;
    }

    public void showCoaches() {
        System.out.println("--- Available Coaches ---");
        for (Coach c : coaches) {
            System.out.println("[ " + c.getCoachId() + " ] " + c.getName());
        }
    }
}