package ca.cmpt213.courseplanner.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassList {
    List<Class> classList;

    public ClassList(SectionList sectionlist){
        classList = new ArrayList<>();

        HashMap<String, Integer> processed = new HashMap<>();
        for (int i = 0; i < sectionlist.getSectionList().size(); ++i) {
            Section currentSection = sectionlist.getSectionList().get(i);
            String name = currentSection.getName();
            int semester = currentSection.getSemester();
            String location = currentSection.getLocation();
            List<Section> list = new ArrayList<>();
            list.add(currentSection);
            String key = name + semester + location;
            if (!processed.containsKey(key)) {
                Class newClass = new Class(name, semester, location, list);
                classList.add(newClass);
                processed.put(key, classList.size() - 1);
            } else {
                Class foundClass = classList.get(processed.get(key));
                foundClass.addSection(currentSection);
            }
        }
    }

    public List<Class> getClassList() {
        return classList;
    }
}
