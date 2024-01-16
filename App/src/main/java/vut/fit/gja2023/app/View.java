package vut.fit.gja2023.app;

public enum View {
    MAIN("views/index"),
    ERROR("views/error"),
    COURSE("views/course"),
    COURSES("views/courses"),
    UPLOAD_STUDENTS("views/course_upload_students"),
    COURSE_STUDENTS("views/course_students"),
    CREATE_ASSIGNMENT("views/course_create_assignment"),
    ASSIGNMENT("views/assignment");

    public final String value;

    private View(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
