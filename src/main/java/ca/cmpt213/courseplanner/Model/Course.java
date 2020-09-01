package ca.cmpt213.courseplanner.Model;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Course implements Cloneable, Iterable<Class>{
    private String name;
    private List<Class> classList;
    private List<CourseWatcher> watchers = new ArrayList<>();

    public Course(String name, List<Class> classList) {
        this.name = name;
        this.classList = new ArrayList<>();
        for (int i = 0; i < classList.size(); i++){
            this.classList.add(classList.get(i));
        }
    }

    public String getName() {
        return name;
    }

    public List<Class> getClassList() {
        return classList;
    }

    public String getSubject() {
        return name.split(" ")[0];
    }

    public String getCatalogNumber() {
        return name.split(" ")[1];
    }

    public Class getClassByIndexId(int id) {
        if (id < 1 || id > classList.size()) {
            throw new IndexOutOfBoundsException("Class id out of bound");
        }
        try{
            return (Class)classList.get(id - 1).clone();
        } catch(Exception e) {
          return null;
        }
    }

    public void addOffering(Section newSection) {
        String classKey = newSection.getName() + newSection.getSemester() + newSection.getLocation();
        Class foundClass = null;
        for (Class tmpClass : classList) {
            if ((tmpClass.getName() + tmpClass.getSemester() + tmpClass.getLocation()).equalsIgnoreCase(classKey)) {
                tmpClass.addSection(newSection);
                foundClass = tmpClass;
                break;
            }
        }
        if (foundClass == null) {
            List<Section> newSectionList = new ArrayList<>();
            newSectionList.add(newSection);
            Class newClass = new Class(newSection.getName(), newSection.getSemester(), newSection.getLocation(), newSectionList);
            foundClass = newClass;
            classList.add(newClass);
        }
        ZonedDateTime dateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z YYYY");
        String event = String.format("%s: Added section %s with enrollment (%d / %d) to offering %s %d",
                dateTime.format(formatter), newSection.getComponentCode(),
                newSection.getEnrolmentTotal(), newSection.getEnrolmentCapacity(),
                foundClass.getTerm(), foundClass.getYear());
        notifyWatchers(event);
    }

    public void registerWatcher(CourseWatcher watcher) {
        watchers.add(watcher);
    }

    private void notifyWatchers(String event) {
        for (CourseWatcher watcher : watchers) {
            watcher.courseChanged(event);
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public Iterator<Class> iterator() {
        return classList.iterator();
    }
}
