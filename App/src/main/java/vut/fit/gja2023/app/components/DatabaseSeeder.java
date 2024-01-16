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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CourseService courseService;

    @Override
    public void run(String... args) throws Exception {
        seedCourses();
    }

    private void seedCourses() {
        var kenobi = new UserBo();
        kenobi.setName("Obi-Wan Kenobi");
        kenobi.setLogin("xkenob01");
        kenobi.setRole(UserRole.TEACHER);

        var vader = new UserBo();
        vader.setName("Darth Vader");
        vader.setLogin("xvader01");
        vader.setRole(UserRole.TEACHER);

        var yoda = new UserBo();
        yoda.setName("Yoda");
        yoda.setLogin("xyodaa01");
        yoda.setRole(UserRole.TEACHER);

        userRepository.saveAll(Arrays.asList(kenobi, vader, yoda));

        courseService.createCourse("Introduction to Breathing", "I2B", vader, vader);
        courseService.createCourse("High Ground Fundamentals", "HGF", kenobi, kenobi);
        courseService.createCourse("Backwards Language Understanding","BLU", yoda, yoda);
    }
}
