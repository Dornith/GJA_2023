package vut.fit.gja2023.app.components;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vut.fit.gja2023.app.entity.CourseBo;
import vut.fit.gja2023.app.entity.ProjectAssignmentBo;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.enums.UserRole;
import vut.fit.gja2023.app.repository.CourseRepository;
import vut.fit.gja2023.app.repository.ProjectAssignmentRepository;
import vut.fit.gja2023.app.repository.ProjectRepository;
import vut.fit.gja2023.app.repository.UserRepository;
import vut.fit.gja2023.app.service.CourseService;
import vut.fit.gja2023.app.service.SystemAdapter;
import vut.fit.gja2023.app.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final UserService userService;
    private final CourseService courseService;

    @Override
    public void run(String... args) throws Exception {
        prepareData();
    }

    private void prepareData() {
        /*
        var yoda = userService.generateUser("xyodaa01", "Yoda", UserRole.TEACHER);
        var kenobi = userService.generateUser("xkenob01", "Obi-Wan Kenobi", UserRole.ADMIN);
        var vader = userService.generateUser("xvader01", "Darth Vader", UserRole.TEACHER);

        courseService.createCourse("Introduction to Breathing", "I2B", vader, vader);
        courseService.createCourse("High Ground Fundamentals", "HGF", kenobi, kenobi);
        courseService.createCourse("Backwards Language Understanding","BLU", yoda, yoda);
         */
    }
}
