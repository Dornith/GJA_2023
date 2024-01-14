package vut.fit.gja2023.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.repository.CourseRepository;
import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final SystemManagerService systemManagerService;

    @Transactional
    public void upsertStudents(long courseId, List<UserBo> newStudents) {
        var courseResult = courseRepository.findById(courseId);
        if (courseResult.isEmpty()) return;

        var course = courseResult.get();
        var existingStudents = course.getStudents();

        var toRemove = existingStudents.stream().filter(isNotIn(newStudents));
        toRemove.forEach(student -> course.getStudents().remove(student));

        var toInsert = newStudents.stream().filter(isNotIn(existingStudents));
        toInsert.forEach(student -> course.getStudents().add(student));

        courseRepository.save(course);

        //TODO: remove users that are not attending other courses
    }

    private static Predicate<UserBo> isNotIn(List<UserBo> users) {
        return uA -> users.stream().noneMatch(uB -> uB.getLogin().equals(uA.getLogin()));
    }
}
