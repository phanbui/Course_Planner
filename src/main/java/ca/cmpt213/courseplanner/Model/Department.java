package ca.cmpt213.courseplanner.Model;

import ca.cmpt213.courseplanner.Controllers.Wrappers.ApiCourseWrapper;
import ca.cmpt213.courseplanner.Controllers.Wrappers.ApiDepartmentWrapper;
import ca.cmpt213.courseplanner.Controllers.Wrappers.ApiWatcherWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Department implements Cloneable{
    private String name;
    private CourseList courseList;

    public Department(String name, CourseList courseList) {
        this.name = name;
        this.courseList = courseList;
    }

    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

   public CourseList getCourseListObject() {
        return courseList;
   }

   public String getName() {
        return name;
   }

   public Course getCourseByIndexId(int id) {
        return courseList.getCourseByIndexId(id);
   }


   public int getTotalStuBySem(int semester) {
        int totalStudents = 0;
        for (Course course : courseList) {
            for (Class tmpClass : course) {
                if (tmpClass.getSemester() == semester) {
                    for (Section section : tmpClass.sections()) {
                        if (section.getComponentCode().equalsIgnoreCase("LEC")) {
                            totalStudents += section.getEnrolmentTotal();
                        }
                    }
                }
            }
        }
        return totalStudents;
   }
}
