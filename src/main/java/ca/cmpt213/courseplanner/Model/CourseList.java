package ca.cmpt213.courseplanner.Model;


//import java.io.FileWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CourseList implements Iterable<Course> {
    private List<Course> courseList = new ArrayList<>();

    public CourseList() {

    }

    public CourseList(ClassList classList) {

        HashMap<String, Integer> processed = new HashMap<>();
        for (int i = 0; i < classList.getClassList().size(); ++i) {
            Class currentClass = classList.getClassList().get(i);
            String name = currentClass.getName();
            List<Class> list = new ArrayList<>();
            list.add(currentClass);
            if (!processed.containsKey(name)) {
                Course newCourse = new Course(name, list);
                courseList.add(newCourse);
                processed.put(name, courseList.size() - 1);
            } else {
                Course foundCourse = courseList.get(processed.get(name));
                foundCourse.getClassList().add(currentClass);
            }
        }

    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public String info() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < courseList.size(); i++) {
            Course currentCourse = courseList.get(i);
            str.append(currentCourse.getName() + "\n");
            for (int j = 0; j < currentCourse.getClassList().size(); j++) {
                Class currentClass = currentCourse.getClassList().get(j);
                str.append("\t  " + currentClass.getSemester() + " in "
                        + currentClass.getLocation() + " by "
                        + currentClass.getInstructorsAsString() + "\n");
//                HashMap<String, Integer> sectionsMap = new HashMap<>();
//                List<Section> groupedSections = new ArrayList<>();
//                for (Section section : currentClass.sections()) {
//                    String key = section.getName() + section.getSemester() +
//                            section.getLocation() + section.getComponentCode();
//                    if (!sectionsMap.containsKey(key)) {
//                        groupedSections.add(new Section(section));
//                        sectionsMap.put(key, groupedSections.size() - 1);
//                    } else {
//                        Section cachedSection = groupedSections.get(sectionsMap.get(key));
//                        cachedSection.setEnrolmentCapacity(cachedSection.getEnrolmentCapacity()
//                                                            + section.getEnrolmentCapacity());
//                        cachedSection.setEnrolmentTotal(cachedSection.getEnrolmentTotal()
//                                                            + section.getEnrolmentTotal());
//
//                    }
//                }
                List<Section> aggregatedSections = currentClass.getAggregatedSections();
                for (int k = 0; k < aggregatedSections.size(); k++) {
                    Section currentSection = aggregatedSections.get(k);
                    str.append("\t\t\tType=" + currentSection.getComponentCode()
                            + ", Enrollment=" + currentSection.getEnrolmentTotal()
                            + "/" + currentSection.getEnrolmentCapacity() + "\n");
                }
            }
        }
        return str.toString();
    }

    public void addCourse(Course course) {
        courseList.add(course);
    }

    public void addOffering(Section newSection) {
        String courseName = newSection.getName();
        boolean isNewCourse = true;
        for (Course course : courseList) {
            if (course.getName().equalsIgnoreCase(courseName)) {
                isNewCourse = false;
                course.addOffering(newSection);
                break;
            }
        }
        if (isNewCourse) {
            List<Section> newSectionList = new ArrayList<>();
            newSectionList.add(newSection);
            List<Class> newClassList = new ArrayList<>();
            newClassList.add(new Class(courseName, newSection.getSemester(), newSection.getLocation(), newSectionList));
            Course newCourse = new Course(courseName, newClassList);
            addCourse(newCourse);
        }
    }

    public Course getCourseByIndexId(int id) {
        if (id < 1 || id > courseList.size()) {
            throw new IndexOutOfBoundsException("Course index out of bound");
        }
        try {
            return (Course) courseList.get(id - 1).clone();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Iterator<Course> iterator() {
        return courseList.iterator();
    }
}
