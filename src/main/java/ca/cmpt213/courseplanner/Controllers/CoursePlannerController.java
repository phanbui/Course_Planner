package ca.cmpt213.courseplanner.Controllers;

import ca.cmpt213.courseplanner.Controllers.Wrappers.*;
import ca.cmpt213.courseplanner.Model.*;
import ca.cmpt213.courseplanner.Model.Class;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CoursePlannerController {
    SectionList allSections = new SectionList("data/course_data_2018.csv");
    ClassList allClasses = new ClassList(allSections);
    CourseList allCourses = new CourseList(allClasses);
    DepartmentList departmentList = new DepartmentList(allCourses);
    List<ApiWatcherWrapper> watcherWrappers = new ArrayList<>();

    @GetMapping("api/about")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiAboutWrapper about() {
        String appName = "Course Planner";
        String authorName = "TRUNGLAM NGUYEN(301326848) - PHAN BUI(301325875)";
        return new ApiAboutWrapper(appName, authorName);
    }

    @GetMapping("api/dump-model")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void dumpModel() {
        String str = allCourses.info();
        System.out.println(str);
    }

    @GetMapping("api/departments")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<ApiDepartmentWrapper> getDepartments() {
        List<ApiDepartmentWrapper> deptWrappers = new ArrayList<>();
        long i = 1;
        for (Department dept : departmentList) {
            ApiDepartmentWrapper wrapper = new ApiDepartmentWrapper();
            wrapper.deptId = i++;
            wrapper.name = dept.getName();
            deptWrappers.add(wrapper);
        }
        deptWrappers.sort((a, b) -> a.name.compareTo(b.name));
        return deptWrappers;
    }

    @GetMapping("api/departments/{id}/courses")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<ApiCourseWrapper> getCourses(@PathVariable("id") int deptId) {
        List<ApiCourseWrapper> courseWrappers = new ArrayList<>();
        Department foundDept = departmentList.getDeptByIndexId(deptId);
        if (foundDept != null) {
            long i = 1;
            for (Course course : foundDept.getCourseListObject()) {
                ApiCourseWrapper wrapper = new ApiCourseWrapper();
                wrapper.courseId = i++;
                wrapper.catalogNumber = course.getCatalogNumber();
                courseWrappers.add(wrapper);
            }
        }
        courseWrappers.sort((a, b) -> a.catalogNumber.compareTo(b.catalogNumber));
        return courseWrappers;
    }

    @GetMapping("api/departments/{deptId}/courses/{courseId}/offerings")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<ApiCourseOfferingWrapper> getCourseOfferings(@PathVariable("deptId") int deptId,
                                                             @PathVariable("courseId") int courseId) {
        List<ApiCourseOfferingWrapper> offeringWrappers = new ArrayList<>();
        Department foundDept = departmentList.getDeptByIndexId(deptId);
        if (foundDept != null) {
            Course foundCourse = foundDept.getCourseByIndexId(courseId);
            if (foundCourse != null) {
                long i = 1;
                for (Class tmpClass : foundCourse) {
                    ApiCourseOfferingWrapper wrapper = new ApiCourseOfferingWrapper();
                    wrapper.courseOfferingId = i++;
                    wrapper.instructors = tmpClass.getInstructorsAsString();
                    wrapper.location = tmpClass.getLocation();
                    wrapper.semesterCode = tmpClass.getSemester();
                    wrapper.year = tmpClass.getYear();
                    wrapper.term = tmpClass.getTerm();
                    offeringWrappers.add(wrapper);
                }
            }
        }
        offeringWrappers.sort((a, b) -> { return (int)b.semesterCode - (int)a.semesterCode; });
        return offeringWrappers;
    }

    @GetMapping("api/departments/{deptId}/courses/{courseId}/offerings/{offeringId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<ApiOfferingSectionWrapper> getCourseOffering(@PathVariable("deptId") int deptId,
                                                             @PathVariable("courseId") int courseId,
                                                             @PathVariable("offeringId") int offeringId) {
        List<ApiOfferingSectionWrapper> offeringWrappers = new ArrayList<>();
        Department foundDept = departmentList.getDeptByIndexId(deptId);
        if (foundDept != null) {
            Course foundCourse = foundDept.getCourseByIndexId(courseId);
            if (foundCourse != null) {
                Class foundClass = foundCourse.getClassByIndexId(offeringId);
                if (foundClass != null) {
                    List<Section> aggregatedSections = foundClass.getAggregatedSections();
                    for (Section section : aggregatedSections) {
                        ApiOfferingSectionWrapper wrapper = new ApiOfferingSectionWrapper();
                        wrapper.type = section.getComponentCode();
                        wrapper.enrollmentCap = section.getEnrolmentCapacity();
                        wrapper.enrollmentTotal = section.getEnrolmentTotal();
                        offeringWrappers.add(wrapper);
                    }
                }
            }
        }
        return offeringWrappers;
    }

    @GetMapping("api/stats/students-per-semester")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<ApiTotalStudentPerSemWrapper> getTotalStuPerSem(@RequestParam int deptId) {
        List<ApiTotalStudentPerSemWrapper> result = new ArrayList<>();
        Department foundDept = departmentList.getDeptByIndexId(deptId);
        if (foundDept == null)
            return result;
        List<Integer> allSemesters = new ArrayList<>();
        Map<Integer, Boolean> cached = new HashMap<>();
        for (Section section : allSections) {
            if (!cached.containsKey(section.getSemester())) {
                cached.put(section.getSemester(), true);
                allSemesters.add(section.getSemester());
            }
        }
        allSemesters.sort(Comparator.naturalOrder());
        for (Integer semester : allSemesters) {
            result.add(new ApiTotalStudentPerSemWrapper(semester, foundDept.getTotalStuBySem(semester)));
        }
        return result;
    }

    @PostMapping("api/addoffering")
    @ResponseStatus(HttpStatus.CREATED)
    public void addOffering(@RequestBody ApiOfferingDataWrapper data) {
        List<String> instructorList = SectionParser.extractInstructors(data.instructor);
        Section newSection = new Section(data.subjectName, data.catalogNumber, Integer.parseInt(data.semester), data.location,
                data.enrollmentCap, data.enrollmentTotal, instructorList, data.component);

        departmentList.addOffering(newSection);
    }

    @PostMapping("api/watchers")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCourseWatcher(@RequestBody ApiCreateWatcherWrapper payload) {
        int deptId = payload.deptId;
        int courseId = payload.courseId;
        Department foundDept = departmentList.getDeptByIndexId(deptId);
        if (foundDept != null) {
            Course foundCourse = foundDept.getCourseListObject().getCourseByIndexId(courseId);
            if (foundCourse != null) {
                ApiCourseWrapper courseWrapper = new ApiCourseWrapper();
                courseWrapper.courseId = courseId;
                courseWrapper.catalogNumber = foundCourse.getCatalogNumber();
                ApiDepartmentWrapper departmentWrapper = new ApiDepartmentWrapper();
                departmentWrapper.deptId = deptId;
                departmentWrapper.name = foundCourse.getName();
                ApiWatcherWrapper watcherWrapper = new ApiWatcherWrapper();
                watcherWrapper.course = courseWrapper;
                watcherWrapper.department = departmentWrapper;
                watcherWrapper.id = watcherWrappers.size() + 1; //index + 1
                watcherWrapper.events = new ArrayList<>();
                watcherWrappers.add(watcherWrapper);
                foundCourse.registerWatcher((String event) -> {
                    System.out.println(event);
                    watcherWrapper.events.add(event);
                });
            }
        }
    }

    @GetMapping("api/watchers")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ApiWatcherWrapper> getWatchers() {
        return watcherWrappers;
    }

    @GetMapping("api/watchers/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiWatcherWrapper getWatcher(@PathVariable("id") int id) {
        if (id < 1 || id > watcherWrappers.size()) {
            throw new IndexOutOfBoundsException("Watcher id out of bound");
        }
        return watcherWrappers.get(id - 1);
    }

    @DeleteMapping("api/watchers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWatcher(@PathVariable("id") int id) {
        if (id < 1 || id > watcherWrappers.size()) {
            throw new IndexOutOfBoundsException("Watcher index out of bound");
        }
        watcherWrappers.remove(id - 1);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND,
            reason = "Resource not found")
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public void badIdExceptionHandler() {

    }

}
