"use strict";
const myAppObj = new Vue({
    el: "#watcherApp",
    data: {
        watchers: [],
        selectedWatcher: null,

        departments: [],
        selectedDept: null,

        courses: [],
        selectedCourse: null,

        new_semester: '',
        new_location: '',
        new_enrollmentTotal: '',
        new_enrollmentCap: '',
        new_component: '',
        new_instructor: '',
    },
    methods: {
        selectWatcher: function(watcher) {
            myAppObj.selectedWatcher = watcher;
            loadWatchers();
        },
        makeWatcher: function() {
            serverCreateWatcher(
                myAppObj.selectedDept.deptId,
                myAppObj.selectedCourse.courseId);
        },
        makeNewSection: function() {
            serverCreateSection(
                myAppObj.new_semester,
                myAppObj.selectedDept.name,
                myAppObj.selectedCourse.catalogNumber,
                myAppObj.new_location,
                myAppObj.new_enrollmentCap,
                myAppObj.new_component,
                myAppObj.new_enrollmentTotal,
                myAppObj.new_instructor
            );
            myAppObj.selectedWatcher = null;
        },
    },
    watch: {
        selectedDept: function (val) {
            console.log("Running course update on department ID " + myAppObj.selectedDept.deptId)
            myAppObj.selectedCourse = null;
            loadDepartmentCourses(myAppObj.selectedDept.deptId);
        },
    }
});

$(document).ready(function() {
    loadWatchers();
    loadDepartments();
});


function loadWatchers() {
    axios.get('/api/watchers', {})
      .then(function (response) {
        console.log(response);
        myAppObj.watchers = response.data;
      })
      .catch(function (error) {
        console.log(error);
      });
}
function loadDepartments() {
    axios.get('/api/departments', {})
      .then(function (response) {
        console.log(response);
        myAppObj.departments = response.data;
      })
      .catch(function (error) {
        console.log(error);
      });
}

function loadDepartmentCourses(deptId) {
    axios.get('/api/departments/' + deptId + "/courses", {})
          .then(function (response) {
            console.log(response);
            myAppObj.courses = response.data;
          })
          .catch(function (error) {
            console.log(error);
          });
}
function serverCreateWatcher(deptId, courseId) {
    axios.post('/api/watchers', {
        deptId: deptId,
        courseId: courseId,
      })
      .then(function (response) {
        console.log(response);
        loadWatchers();
      })
      .catch(function (error) {
        console.log(error);
      });
}
function serverCreateSection(
    semester,
    subjectName,
    catalogNumber,
    location,
    enrollmentCap,
    component,
    enrollmentTotal,
    instructor
) {
    axios.post('/api/addoffering', {
        semester: semester,
        subjectName: subjectName,
        catalogNumber: catalogNumber,
        location: location,
        enrollmentCap: enrollmentCap,
        component: component,
        enrollmentTotal: enrollmentTotal,
        instructor: instructor
      })
      .then(function (response) {
        console.log(response);
        loadWatchers();
      })
      .catch(function (error) {
        console.log(error);
        window.alert("Add offering/section failed: "
            + error
        );
      });
}
