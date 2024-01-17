package vut.fit.gja2023.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vut.fit.gja2023.app.entity.CourseBo;
import vut.fit.gja2023.app.enums.Layout;
import vut.fit.gja2023.app.enums.UserRole;
import vut.fit.gja2023.app.enums.View;
import vut.fit.gja2023.app.models.CsvColumn;
import vut.fit.gja2023.app.repository.CourseRepository;
import vut.fit.gja2023.app.repository.ProjectAssignmentRepository;
import vut.fit.gja2023.app.service.CourseService;
import vut.fit.gja2023.app.service.StudentCsvParser;
import vut.fit.gja2023.app.service.UserService;
import vut.fit.gja2023.app.util.CsvReaderConfig;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import vut.fit.gja2023.app.service.TeamsCsvParser;

@Controller
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final StudentCsvParser studentCsvParser;
    private final TeamsCsvParser teamsCsvParser;
    private final ProjectAssignmentRepository projectAssignmentRepository;
    private final UserService userService;

    private static final String LOGIN_COLUMN_ORDER = "csv-login";
    private static final String NAME_COLUMN_ORDER = "csv-name";

    private static final List<CsvColumn> studentsCsvColumns = Arrays.asList(
        new CsvColumn("Login", LOGIN_COLUMN_ORDER),
        new CsvColumn("Name", NAME_COLUMN_ORDER)
    );

    private static final String TEAM_COLUMN_ORDER = "csv-team";
    
    private static final List<CsvColumn> teamsCsvColumns = Arrays.asList(
        new CsvColumn("Login", LOGIN_COLUMN_ORDER),
        new CsvColumn("Team", TEAM_COLUMN_ORDER)
    );

    @GetMapping
    public String getCourses(Model model) {
        var userResult = userService.getLoggedInUser();
        if (userResult.isEmpty()) {
            return "redirect:/perform-logout";
        }

        var user = userResult.get();

        List<CourseBo> courses;
        if (user.getRole() == UserRole.ADMIN) {
            courses = courseRepository.findAll();
        } else if (user.getRole() == UserRole.TEACHER) {
            courses = userService.getTeachingCourses(user);
        } else if (user.getRole() == UserRole.STUDENT) {
            courses = user.getStudiedCourses();
        } else {
            return "redirect:/perform-logout";
        }

        model.addAttribute("courses", courses);
        model.setTargetView(View.COURSES);
        return Layout.DEFAULT.getValue();
    }

    /**
     * Retrieve course information using its ID.
     * 
     * @param model Model
     * @param courseId The ID of a course.
     */
    @GetMapping("/{courseId}")
    public String getCourse(Model model, @PathVariable long courseId) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        model.addAttribute("course", course.get());
        model.setTargetView(View.COURSE);
        return Layout.DEFAULT.getValue();
    }

    /**
     * Retrieve all students registered for a course.
     * 
     * @param model Model
     * @param courseId The ID of a course.
     */
    @GetMapping("/{courseId}/students")
    public String getStudents(Model model, @PathVariable long courseId) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        model.addAttribute("course", course.get());
        model.setTargetView(View.COURSE_STUDENTS);
        return Layout.DEFAULT.getValue();
    }

    /**
     * Retrieves a form used for uploading students to the system.
     *
     * 
     * @param model Model
     * @param courseId The ID of a course.
     */
    @GetMapping("/{courseId}/students/upload")
    public String uploadStudents(Model model, @PathVariable long courseId) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        model.addAttribute("course", course.get());
        model.setCsvColumns(studentsCsvColumns);
        model.setTargetView(View.UPLOAD_STUDENTS);
        return Layout.DEFAULT.getValue();
    }

    /**
     * Uploads a list of students from a csv file to a specified course to the system.
     * 
     * @param model Model
     * @param courseId The ID of a course.
     * @param csvFile Csv file to read the students from.
     * @param separator Separator used in the csv file.
     * @param hasHeader Whether the csv file has a header row or not.
     * @param login The position of the user login column.
     * @param name The position of the team name column.
     */
    @PostMapping("/{courseId}/students/upload")
    public String uploadStudents(
        Model model,
        @PathVariable long courseId,
        @RequestParam("csv-file") MultipartFile csvFile,
        @RequestParam("csv-separator") String separator,
        @RequestParam("csv-has-header") boolean hasHeader,
        @RequestParam(LOGIN_COLUMN_ORDER) int login,
        @RequestParam(NAME_COLUMN_ORDER) int name
    ) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        if (csvFile.isEmpty() || separator.length() != 1) {
            model.setErrorView("401", "Bad request.");
            return Layout.DEFAULT.getValue();
        }

        var config = new CsvReaderConfig(new int[]{ login - 1, name - 1 }, hasHeader, separator.charAt(0));
        var students = studentCsvParser.parse(csvFile, config);
        courseService.upsertStudents(course.get(), students);

        return "redirect:/courses/" + courseId + "/students";
    }

    /**
     * Retrieves a form used for creating assignments in a specified course.
     * 
     * @param model Model
     * @param courseId The ID of a course.
     */
    @GetMapping("/{courseId}/assignments/create")
    public String createAssignment(Model model, @PathVariable long courseId) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        model.addAttribute("course", course.get());
        model.setTargetView(View.CREATE_ASSIGNMENT);
        return Layout.DEFAULT.getValue();
    }

    /**
     * Creates an assignment for a given course.
     * 
     * @param model Model
     * @param courseId The ID of a course.
     * @param title The title of the assignment.
     * @param deadline The deadline for the assignment.
     * @param description A description of the assignment.
     * @param isTeam Whether this is a team assignment or an individual one.
     */
    @PostMapping("/{courseId}/assignments/create")
    public String createAssignment(
        Model model,
        @PathVariable long courseId,
        @RequestParam("ass-title") String title,
        @RequestParam("ass-deadline") @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline,
        @RequestParam("ass-desc") String description,
        @RequestParam("ass-team") boolean isTeam
    ) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        courseService.addAssignmentToCourse(course.get(), title, description, deadline, isTeam);
        return "redirect:/courses/" + courseId;
    }

    /**
     * Deletes a specified assignment in a given course.
     * 
     * @param model Model
     * @param courseId The ID of a course.
     * @param assignmentId The ID of and assignment.
     */
    @PostMapping("/{courseId}/assignments/{assignmentId}/delete")
    public String deleteAssignment(Model model, @PathVariable long courseId, @PathVariable long assignmentId) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        var assignment = projectAssignmentRepository.findById(assignmentId);
        if (assignment.isEmpty()) {
            model.setErrorView("404", "Project assignment doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        courseService.removeAssignmentFromCourse(course.get(), assignment.get());
        return "redirect:/courses/" + courseId;
    }

    /**
     * Retrieves information about a specified assignment in a given course.
     * 
     * @param model Model
     * @param courseId The ID of a course.
     * @param assignmentId The ID of an assignment.
     * @return 
     */
    @GetMapping("/{courseId}/assignments/{assignmentId}")
    public String getAssignment(Model model, @PathVariable long courseId, @PathVariable long assignmentId) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        var assignment = projectAssignmentRepository.findById(assignmentId);
        if (assignment.isEmpty()) {
            model.setErrorView("404", "Project assignment doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        model.addAttribute("course", course.get());
        model.addAttribute("assignment", assignment.get());
        model.setTargetView(View.ASSIGNMENT);
        return Layout.DEFAULT.getValue();
    }

    @GetMapping("/{courseId}/teams")
    public String getTeams(Model model, @PathVariable long courseId) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        model.addAttribute("course", course.get());
        model.setTargetView(View.COURSE_TEAMS);
        return Layout.DEFAULT.getValue();
    }

    @GetMapping("/{courseId}/teams/upload")
    public String uploadTeams(Model model, @PathVariable long courseId) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        model.addAttribute("course", course.get());
        model.setCsvColumns(teamsCsvColumns);
        model.setTargetView(View.UPLOAD_TEAMS);
        return Layout.DEFAULT.getValue();
    }

    @PostMapping("/{courseId}/teams/upload")
    public String uploadTeams(
        Model model,
        @PathVariable long courseId,
        @RequestParam("csv-file") MultipartFile csvFile,
        @RequestParam("csv-separator") String separator,
        @RequestParam("csv-has-header") boolean hasHeader,
        @RequestParam(LOGIN_COLUMN_ORDER) int login,
        @RequestParam(TEAM_COLUMN_ORDER) int teamName
    ) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.getValue();
        }

        if (csvFile.isEmpty() || separator.length() != 1) {
            model.setErrorView("401", "Bad request.");
            return Layout.DEFAULT.getValue();
        }

        CsvReaderConfig config = new CsvReaderConfig(new int[]{ teamName - 1, login - 1 }, hasHeader, separator.charAt(0));
        Map<String, List<String>> studentsTeams = teamsCsvParser.parse(csvFile, config);
        courseService.upsertTeams(course.get(), studentsTeams);

        return "redirect:/courses/" + courseId + "/teams";
    }
}
