package vut.fit.gja2023.app.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum View {
    MAIN("views/index"),
    ERROR("views/error"),
    COURSE("views/course"),
    COURSES("views/courses"),
    UPLOAD_STUDENTS("views/course_upload_students"),
    COURSE_STUDENTS("views/course_students"),
    CREATE_ASSIGNMENT("views/course_create_assignment"),
    ASSIGNMENT("views/assignment"),
    LOGIN("views/login");

    public final String value;
}
