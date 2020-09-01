package ca.cmpt213.courseplanner.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Section {
    private String name;
    private String subject;
    private String catalogNumber;
    private int semester;
    private String location;
    private int enrolmentCapacity;
    private int enrolmentTotal;
    private List<String> instructors;
    private String componentCode;

    public Section(String subject, String catalogNumber, int semester, String location, int enrolmentCapacity, int enrolmentTotal, List<String> instructors, String componentCode) {
        this.subject = subject;
        this.catalogNumber = catalogNumber;
        this.name = subject + " " + catalogNumber;
        this.semester = semester;
        this.location = location;
        this.enrolmentCapacity = enrolmentCapacity;
        this.enrolmentTotal = enrolmentTotal;
        this.instructors = instructors;
        this.componentCode = componentCode.trim();
    }

    public Section(Section section) {
        this.subject = section.getSubject();
        this.catalogNumber = section.getCatalogNumber();
        this.name = section.getName();
        this.semester = section.getSemester();
        this.location = section.getLocation();
        this.enrolmentCapacity = section.getEnrolmentCapacity();
        this.enrolmentTotal = section.getEnrolmentTotal();
        this.instructors = new ArrayList<>();
        this.instructors.addAll(section.getInstructors());
        this.componentCode = section.getComponentCode();
    }

    public String getName() {
        return name;
    }

    public int getSemester() {
        return semester;
    }

    public String getLocation() {
        return location;
    }

    public int getEnrolmentCapacity() {
        return enrolmentCapacity;
    }

    public int getEnrolmentTotal() {
        return enrolmentTotal;
    }

    public String getInstructorsAsString() {
        if (instructors.isEmpty()) {
            return "";
        }
        String result = instructors.get(0);
        for (int i = 0; i < instructors.size() - 1; ++i) {
            result += ", " + instructors.get(i+1);
        }
        return result;
    }

    public List<String> getInstructors() {
        return Collections.unmodifiableList(instructors);
    }

    public void addInstructor(String newInstructor) {
        instructors.add(newInstructor);
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setEnrolmentCapacity(int newCapacity) {
        enrolmentCapacity = newCapacity;
    }

    public void setEnrolmentTotal(int enrolmentTotal) {
        this.enrolmentTotal = enrolmentTotal;
    }

    public String getSubject() {
        return subject;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

}
