package ca.cmpt213.courseplanner.Model;

import ca.cmpt213.courseplanner.Controllers.Wrappers.ApiCourseWrapper;
import ca.cmpt213.courseplanner.Controllers.Wrappers.ApiDepartmentWrapper;
import ca.cmpt213.courseplanner.Controllers.Wrappers.ApiWatcherWrapper;

import java.util.*;

public class DepartmentList implements Iterable<Department>{
    private List<Department> departments = new ArrayList<>();

    public DepartmentList(CourseList courstList) {
        HashMap<String, Integer> processed = new HashMap<>();
        for (int i = 0; i < courstList.getCourseList().size(); ++i) {
            Course currentCourse = courstList.getCourseList().get(i);
            String name = currentCourse.getSubject();
            if (!processed.containsKey(name)) {
                CourseList courses = new CourseList();
                courses.addCourse(currentCourse);
                Department department = new Department(name, courses);
                departments.add(department);
                processed.put(name, departments.size() - 1);
            } else {
                Department foundDept = departments.get(processed.get(name));
                foundDept.getCourseListObject().addCourse(currentCourse);
            }
        }
    }

    public void addDepartment(Department dept) {
        departments.add(dept);
    }

    public Department getDeptByIndexId(int index){
        if (index < 1 || index > departments.size()) {
           throw new IndexOutOfBoundsException("Department index is out of bound");
        }
        try {
            return (Department)departments.get(index - 1).clone();
        }catch (CloneNotSupportedException e) {
            return null;
        }
    }


    public void addOffering(Section newSection) {
        String deptName = newSection.getSubject();
        String courseName = newSection.getName();
        boolean isNewDept = true;
        for (Department dept : departments) {
            if (dept.getName().equalsIgnoreCase(deptName)) {
                isNewDept = false;
                dept.getCourseListObject().addOffering(newSection);
                break;
            }
        }

        if (isNewDept) {
            List<Section> newSectionList = new ArrayList<>();
            newSectionList.add(newSection);
            List<Class> newClassList = new ArrayList<>();
            newClassList.add(new Class(courseName, newSection.getSemester(), newSection.getLocation(), newSectionList));
            CourseList newCourseList = new CourseList();
            newCourseList.addCourse(new Course(courseName, newClassList));
            Department newDept = new Department(newSection.getSubject(), newCourseList);
            this.addDepartment(newDept);
        }
    }


    @Override
    public Iterator<Department> iterator() {
        return departments.iterator();
    }
}
