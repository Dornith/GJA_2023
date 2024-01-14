package vut.fit.gja2023.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vut.fit.gja2023.app.Layout;
import vut.fit.gja2023.app.View;
import vut.fit.gja2023.app.models.CsvColumn;
import vut.fit.gja2023.app.repository.CourseRepository;
import vut.fit.gja2023.app.service.CourseService;
import vut.fit.gja2023.app.service.StudentCsvParser;
import vut.fit.gja2023.app.util.CsvReaderConfig;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final StudentCsvParser csvParser;

    private static final String LOGIN_COLUMN_ORDER = "csv-login";
    private static final String NAME_COLUMN_ORDER = "csv-name";

    private static final List<CsvColumn> csvColumns = Arrays.asList(
        new CsvColumn("Login", LOGIN_COLUMN_ORDER),
        new CsvColumn("Name", NAME_COLUMN_ORDER)
    );

    @GetMapping
    public String getCourses(Model model) {
        //TODO: get only courses of logged user
        var courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        model.setTargetView(View.COURSES);
        return Layout.DEFAULT.toString();
    }

    @GetMapping("/{courseId}")
    public String getCourse(Model model, @PathVariable long courseId) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.toString();
        }

        model.addAttribute("course", course.get());
        model.setTargetView(View.COURSE);
        return Layout.DEFAULT.toString();
    }

    @GetMapping("/{courseId}/students")
    public String getStudents(Model model, @PathVariable long courseId) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.toString();
        }

        model.addAttribute("course", course.get());
        model.setTargetView(View.COURSE_STUDENTS);
        return Layout.DEFAULT.toString();
    }

    @GetMapping("/{courseId}/students/upload")
    public String uploadStudents(Model model, @PathVariable long courseId) {
        var course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            model.setErrorView("404", "Course doesn't exist.");
            return Layout.DEFAULT.toString();
        }

        model.addAttribute("course", course.get());
        model.setCsvColumns(csvColumns);
        model.setTargetView(View.UPLOAD_STUDENTS);
        return Layout.DEFAULT.toString();
    }

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
            return Layout.DEFAULT.toString();
        }

        if (csvFile.isEmpty() || separator.length() != 1) {
            model.setErrorView("401", "Bad request.");
            return Layout.DEFAULT.toString();
        }

        var config = new CsvReaderConfig(new int[]{ login - 1, name - 1 }, hasHeader, separator.charAt(0));
        var students = csvParser.parse(csvFile, config);
        courseService.upsertStudents(course.get(), students);

        return "redirect:/courses/" + courseId + "/students";
    }
}
