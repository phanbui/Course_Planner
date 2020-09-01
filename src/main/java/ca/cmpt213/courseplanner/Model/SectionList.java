package ca.cmpt213.courseplanner.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SectionList implements Iterable<Section> {
    private List<Section> sectionList = new ArrayList<>();

    public SectionList(String filePath) {
        this.sectionList = new SectionParser(filePath).getParsedSectionList();
    }

    public List<Section> getSectionList() {
        return Collections.unmodifiableList(sectionList);
    }

    @Override
    public Iterator<Section> iterator() {
        return sectionList.iterator();
    }
}
