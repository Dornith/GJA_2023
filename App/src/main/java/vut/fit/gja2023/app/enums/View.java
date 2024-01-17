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
    UPLOAD_TEAMS("views/course_upload_teams"),
    COURSE_STUDENTS("views/course_students"),
    CREATE_ASSIGNMENT("views/course_create_assignment"),
    ASSIGNMENT("views/assignment"),
    LOGIN("views/login"),
    COURSE_TEAMS("views/course_teams");

    public final String value;
}
