package vut.fit.gja2023.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.CourseBo;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.repository.CourseRepository;
import vut.fit.gja2023.app.repository.UserRepository;
import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SystemManagerService systemManagerService;

    @Transactional
    public void upsertStudents(CourseBo course, List<UserBo> newStudents) {
        var currentCourseStudents = course.getStudents();
        var logins = newStudents.stream().map(UserBo::getLogin).toList();
        var existingStudents = userRepository.findAllByLogins(logins);

        //students that don't exist
        var toCreate = newStudents.stream()
            .filter(isNotIn(existingStudents))
            .peek(student -> student.getStudiedCourses().add(course))
            .toList();
        //TODO:
        // - create users in OS (profile, directories ...)
        // - generate password and send email

        //students that newly attend the course
        var toAdd = existingStudents.stream()
            .filter(isNotIn(currentCourseStudents))
            .peek(student -> student.getStudiedCourses().add(course))
            .toList();

        //students that have updated name
        var toUpdate = existingStudents.stream()
            .filter(isNotIn(toCreate))
            .peek(student -> student.setName(newStudents.stream()
                .filter(newStudent -> newStudent.getLogin().equals(student.getLogin()))
                .toList()
                .getFirst()
                .getName()))
            .toList();

        //students that don't attend the course anymore
        var toRemove = currentCourseStudents.stream()
            .filter(isNotIn(newStudents))
            .peek(student -> removeStudentFromCourse(course, student))
            .toList();

        //students that don't attend other courses
        var toDelete = toRemove.stream()
            .filter(student -> student.getStudiedCourses().isEmpty()).toList();
        //TODO:
        // - delete firewall rules from OS
        // - delete user's profile and folders from OS
        // - potentially delete teams (and their projects and folders in OS)

        courseRepository.save(course);
        userRepository.saveAll(toCreate);
        userRepository.saveAll(toAdd);
        userRepository.saveAll(toUpdate);
        userRepository.saveAll(toRemove);
        userRepository.deleteAllInBatch(toDelete);
    }

    private static Predicate<UserBo> isNotIn(List<UserBo> users) {
        return uA -> users.stream().noneMatch(uB -> uB.getLogin().equals(uA.getLogin()));
    }

    private static void removeStudentFromCourse(CourseBo course, UserBo student) {
        student.getStudiedCourses().remove(course);

        var courseTeams = student.getTeams().stream()
            .filter(team -> team.getCourse().getId().equals(course.getId()));

        courseTeams.forEach(team -> {
            student.getTeams().remove(team);
            if (team.getMembers().size() == 1) {
                //TODO: remove team from DB and OS
            }
        });
    }
}
