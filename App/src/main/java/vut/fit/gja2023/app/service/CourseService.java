package vut.fit.gja2023.app.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.CourseBo;
import vut.fit.gja2023.app.entity.ProjectAssignmentBo;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.enums.UserRole;
import vut.fit.gja2023.app.repository.CourseRepository;
import vut.fit.gja2023.app.repository.ProjectAssignmentRepository;
import vut.fit.gja2023.app.repository.UserRepository;
import vut.fit.gja2023.app.util.OSNameParser;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ProjectAssignmentRepository assignmentRepository;
    private final SystemAdapter systemAdapter;
    private final UserService userService;
    private final TeamService teamService;
    private final ProjectService projectService;

    @Transactional
    public void createCourse(
        @NotNull String name,
        @NotNull String abb,
        @NotNull UserBo guarantor,
        @NotNull UserBo coordinator
    ) {
        var course = new CourseBo();
        course.setName(name);
        course.setAbb(abb);
        course.setGuarantor(guarantor);
        course.setCoordinator(coordinator);
        courseRepository.save(course);
        systemAdapter.createCourse(course);
    }

    @Transactional
    public void upsertStudents(@NotNull CourseBo course, @NotNull List<UserBo> newStudents) {
        for (var newStudent : newStudents) {
            var existingStudentResult = userRepository.findByUserLogin(newStudent.getLogin());

            //student already exists
            if (existingStudentResult.isPresent()) {
                var existingStudent = existingStudentResult.get();
                existingStudent.setName(newStudent.getName());

                //student started attending course
                if (!existingStudent.getStudiedCourses().contains(course)) {
                    addStudentToCourse(course, existingStudent);
                }
            } else {
                //student has to be created
                var generatedUser = userService.generateUser(newStudent.getLogin(), newStudent.getName(), UserRole.STUDENT);
                addStudentToCourse(course, generatedUser);
            }
        }

        //students that don't attend the course anymore
        course.getStudents().stream()
            .filter(isNotIn(newStudents))
            .forEach(student -> removeStudentFromCourse(course, student));

        courseRepository.save(course);
    }

    private static Predicate<UserBo> isNotIn(List<UserBo> users) {
        return uA -> users.stream().noneMatch(uB -> uB.getLogin().equals(uA.getLogin()));
    }

    @Transactional
    public void removeStudentFromCourse(@NotNull CourseBo course, @NotNull UserBo student) {
        student.getStudiedCourses().remove(course);

        //remove one man projects to this course
        student.getProjects().stream()
            .filter(project -> project.getAssignment().getCourse().getId().equals(course.getId()))
            .forEach(projectService::removeProject);

        //remove student from teams in this course
        student.getTeams().stream()
            .filter(team -> team.getCourse().getId().equals(course.getId()))
            .forEach(team -> teamService.removeStudentFromTeam(team, student));

        if (student.getStudiedCourses().isEmpty()) {
            userService.deleteStudent(student);
        } else {
            userRepository.save(student);
        }
    }

    @Transactional
    public void addStudentToCourse(@NotNull CourseBo course, @NotNull UserBo student) {
        student.getStudiedCourses().add(course);

        //create projects for each non-team assignment
        course.getProjectAssignments().stream()
            .filter(assignment -> !assignment.isTeam())
            .forEach(assignment -> student.getProjects().add(projectService.createProject(student, assignment)));

        userRepository.save(student);
    }

    @Transactional
    public void addAssignmentToCourse(
        @NotNull CourseBo course,
        @NotNull String title,
        @NotNull String description,
        @NotNull Date deadline, boolean isTeam
    ) {
        var assignment = new ProjectAssignmentBo();
        assignment.setName(OSNameParser.toOS(title));
        assignment.setTitle(title);
        assignment.setDeadline(deadline);
        assignment.setTeam(isTeam);
        assignment.setDescription(description);
        assignment.setCourse(course);
        assignmentRepository.save(assignment);

        course.getProjectAssignments().add(assignment);
        courseRepository.save(course);

        if (course.getStudents().isEmpty()) {
            return;
        }

        systemAdapter.createAssignment(assignment);

        if (isTeam) {
            //create project for each student
            course.getStudents().forEach(student -> projectService.createProject(student, assignment));
        } else {
            //create project for each team
            course.getTeams().forEach(team -> projectService.createTeamProject(team, assignment));
        }
    }
}
