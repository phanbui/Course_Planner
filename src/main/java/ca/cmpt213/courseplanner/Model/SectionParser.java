package ca.cmpt213.courseplanner.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SectionParser {
    Map<String, Integer> sectionsCached = new HashMap<>();
    List<Section> sectionList = new ArrayList<>();
    String filePath;

    public SectionParser() {

    }

    static public List<String> extractInstructors(String instructors) {
        List<String> instructorList = new ArrayList<>();
        if (!instructors.equalsIgnoreCase("")) {
            String[] splitted = instructors.split(",");
            for (int i = 0; i < splitted.length; ++i) {
                splitted[i] = splitted[i].trim();
            }
            instructorList.addAll(Arrays.asList(splitted));
        }
        return instructorList;
    }

    public SectionParser(String filePath) {
        this.filePath = filePath;
        processRows();
    }

    public List<String> extractFields(String str) {
        List<String> tokens = new ArrayList<>(Arrays.asList(str.split(",")));
        for (int i = 0; i < tokens.size(); ++i) {
            tokens.set(i, tokens.get(i).trim());
        }
        String currentToken = tokens.get(6);
        if (currentToken.equalsIgnoreCase("<null>")) {
            currentToken = "";
        } else if (currentToken.charAt(0) == '\"') {
            int j = 7;
            currentToken = currentToken.substring(1);
            while (j < tokens.size()) {
                String tmp = tokens.get(j).trim();
                if (!(tmp.charAt(tmp.length() - 1) == '\"')) {
                    currentToken += "," + tmp;
                    tokens.remove(j);
                } else {
                    currentToken += "," + tmp.substring(0, tmp.length() - 1).trim();
                    tokens.remove(j);
                    break;
                }
            }
        }
        tokens.set(6, currentToken);
        return tokens;
    }

    public Section parseFields(List<String> tokens) {
        int semester = Integer.parseInt(tokens.get(0));
        String subject = tokens.get(1);
        String catalogNumber = tokens.get(2);
        String location = tokens.get(3);
        int enrolmentCapacity = Integer.parseInt(tokens.get(4));
        int enrolmentTotal = Integer.parseInt(tokens.get(5));
        List<String> instructorList = extractInstructors(tokens.get(6));
        String componentCode = tokens.get(7);
        return new Section(subject, catalogNumber, semester, location, enrolmentCapacity, enrolmentTotal, instructorList, componentCode);

    }

    public void updateSectionList(Section newSection) {
        sectionList.add(newSection);
    }

    public void processRow(String str) {

        List<String> tokens = extractFields(str);
        Section parsedSection = parseFields(tokens);
        updateSectionList(parsedSection);
    }

    public void processRows() {
        File file = new File(filePath);
        try {
            Scanner inputStream = new Scanner(file);
            inputStream.nextLine();
            while (inputStream.hasNext()) {
                String str = inputStream.nextLine();
                processRow(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Section> getParsedSectionList() {
        return sectionList;
    }
}
