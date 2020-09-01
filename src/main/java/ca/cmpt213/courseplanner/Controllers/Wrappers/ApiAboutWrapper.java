package ca.cmpt213.courseplanner.Controllers.Wrappers;

public class ApiAboutWrapper {
    public String appName;
    public String authorName;

    public ApiAboutWrapper(String appName, String authorName) {
        this.appName = appName;
        this.authorName = authorName;
    }
}
