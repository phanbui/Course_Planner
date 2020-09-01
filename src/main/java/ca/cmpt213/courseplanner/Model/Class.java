package ca.cmpt213.courseplanner.Model;

import java.util.*;

public class Class implements Cloneable{
    private String name;
    private int semester;
    private int year;
    private String term;
    private String location;
    private List<String> instructors = new ArrayList<>();
    private List<Section> sections = new ArrayList<>();

    public Class(String name, int semester, String location,  List<Section> sections) {
        this.name = name;
        this.semester = semester;
        this.year = 1900 + 100*(semester/1000) + 10*((semester/100) % 10) + (semester/10)%10;
        switch (semester % 10) {
            case 1:
                this.term = "Spring";
                break;
            case 4:
                this.term = "Summer";
                break;
            case 7:
                this.term = "Fall";
                break;
        }
        this.location = location;

        for (int i = 0; i < sections.size(); i++){
            addSection(sections.get(i));
        }
    }

    private List<Section> aggregateSections(List<Section> sections) {
        List<Section> groupedSections = new ArrayList<>();
        HashMap<String, Integer> sectionsMap = new HashMap<>();
        for (Section section : sections) {
            String key = section.getName() + section.getSemester() +
                    section.getLocation() + section.getComponentCode();
            if (!sectionsMap.containsKey(key)) {
                groupedSections.add(new Section(section));
                sectionsMap.put(key, groupedSections.size() - 1);
            } else {
                Section cachedSection = groupedSections.get(sectionsMap.get(key));
                cachedSection.setEnrolmentCapacity(cachedSection.getEnrolmentCapacity()
                        + section.getEnrolmentCapacity());
                cachedSection.setEnrolmentTotal(cachedSection.getEnrolmentTotal()
                        + section.getEnrolmentTotal());
            }
        }
        return groupedSections;
    }

    public List<Section> getAggregatedSections() {
        return aggregateSections(this.sections);
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

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public void addSection(Section newSection) {
        sections.add(newSection);
        for (String instructor : newSection.getInstructors()) {
            if (!instructors.contains(instructor)) {
                instructors.add(instructor);
            }
        }
    }

    public int getYear() {
        return year;
    }

    public String getTerm() {
        return term;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Iterable<Section> sections() {
        return new Iterable<Section>() {
            @Override
            public Iterator<Section> iterator() {
                return sections.iterator();
            }
        };
    }

}
