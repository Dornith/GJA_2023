package vut.fit.gja2023.app.components;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vut.fit.gja2023.app.entity.CourseBo;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.enums.UserRole;
import vut.fit.gja2023.app.repository.CourseRepository;
import vut.fit.gja2023.app.repository.UserRepository;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

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

        userRepository.save(kenobi);
        userRepository.save(vader);
        userRepository.save(yoda);

        var breathing = new CourseBo();
        breathing.setName("Introduction to Breathing");
        breathing.setGuarantor(vader);
        breathing.setCoordinator(vader);

        var highGround = new CourseBo();
        highGround.setName("High Ground Fundamentals");
        highGround.setGuarantor(kenobi);
        highGround.setCoordinator(kenobi);

        var lang = new CourseBo();
        lang.setName("Backwards Language Understanding");
        lang.setGuarantor(yoda);
        lang.setCoordinator(yoda);

        courseRepository.save(breathing);
        courseRepository.save(highGround);
        courseRepository.save(lang);

        kenobi.setStudiedCourses(List.of(lang));
        userRepository.save(kenobi);

        vader.setStudiedCourses(Arrays.asList(lang, highGround));
        userRepository.save(vader);
    }
}
