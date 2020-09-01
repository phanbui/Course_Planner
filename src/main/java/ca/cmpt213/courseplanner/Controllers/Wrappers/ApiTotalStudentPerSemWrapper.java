package ca.cmpt213.courseplanner.Controllers.Wrappers;

public class ApiTotalStudentPerSemWrapper {
        public int semesterCode;
        public int totalCoursesTaken;
        public ApiTotalStudentPerSemWrapper(int semesterCode, int totalCoursesTaken) {
                this.semesterCode = semesterCode;
                this.totalCoursesTaken = totalCoursesTaken;
        }
}
