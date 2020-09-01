package ca.cmpt213.courseplanner.Controllers.Wrappers;

import java.util.List;

public class ApiWatcherWrapper {
    public long id;
    public ApiDepartmentWrapper department;
    public ApiCourseWrapper course;
    public List<String> events;
}
