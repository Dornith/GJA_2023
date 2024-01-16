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
import vut.fit.gja2023.app.service.SystemAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ProjectAssignmentRepository projectAssignmentRepository;

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

        var breathing = new CourseBo();
        breathing.setName("Introduction to Breathing");
        breathing.setAbb("I2B");
        breathing.setGuarantor(vader);
        breathing.setCoordinator(vader);

        var highGround = new CourseBo();
        highGround.setName("High Ground Fundamentals");
        highGround.setAbb("HGF");
        highGround.setGuarantor(kenobi);
        highGround.setCoordinator(kenobi);

        var lang = new CourseBo();
        lang.setName("Backwards Language Understanding");
        lang.setAbb("BLU");
        lang.setGuarantor(yoda);
        lang.setCoordinator(yoda);

        courseRepository.saveAll(Arrays.asList(breathing, highGround, lang));

        var assignment = new ProjectAssignmentBo();
        assignment.setCourse(highGround);
        assignment.setName("ABS");
        assignment.setTitle("Dealing in Absolutes");
        assignment.setDescription("");
        assignment.setDeadline(new Date());
        assignment.setTeam(true);

        projectAssignmentRepository.save(assignment);
    }
}
