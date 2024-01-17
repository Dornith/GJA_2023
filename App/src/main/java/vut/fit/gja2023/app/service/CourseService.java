package vut.fit.gja2023.app.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
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
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import vut.fit.gja2023.app.entity.TeamBo;
import vut.fit.gja2023.app.repository.TeamRepository;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ProjectAssignmentRepository assignmentRepository;
    private final SystemAdapter systemAdapter;
    private final UserService userService;
    private final TeamService teamService;
    private final ProjectService projectService;
    private final SystemManagerService systemManager;

    private static Predicate<UserBo> isNotIn(List<UserBo> users) {
        return uA -> users.stream().noneMatch(uB -> uB.getLogin().equals(uA.getLogin()));
    }

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

    public void upsertStudents(@NotNull CourseBo course, @NotNull List<UserBo> newStudents) {
        var logins = newStudents.stream().map(UserBo::getLogin).toList();
        var existingStudents = userRepository.findAllByLogins(logins);
        var courseStudents = course.getStudents();

        for (var newStudent : newStudents) {
            var existingStudentResult = existingStudents.stream()
                .filter(student -> newStudent.getLogin().matches(student.getLogin()))
                .findFirst();

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
                var generatedStudent = userService.generateUser(newStudent.getLogin(), newStudent.getName(), UserRole.STUDENT);
                addStudentToCourse(course, generatedStudent);
            }
        }

        //students that don't attend the course anymore
        courseStudents.stream()
            .filter(isNotIn(newStudents))
            .forEach(student -> removeStudentFromCourse(course, student));
    }

    private void removeStudentFromCourse(@NotNull CourseBo course, @NotNull UserBo student) {
        student.getStudiedCourses().remove(course);

        //remove one man projects to this course
        var toRemove = student.getProjects().stream()
            .filter(project -> project.getAssignment().getCourse().getId().equals(course.getId()))
            .peek(projectService::removeProject).toList();
        student.getProjects().removeIf(toRemove::contains);

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

    public void addStudentToCourse(@NotNull CourseBo course, @NotNull UserBo student) {
        student.getStudiedCourses().add(course);
        userRepository.save(student);

        //create projects for each non-team assignment
        course.getProjectAssignments().stream()
            .filter(assignment -> !assignment.isTeam())
            .forEach(assignment -> projectService.createProject(student, assignment));
    }
    
    public void upsertTeams(@NotNull CourseBo course, @NotNull Map<String, List<String>> teamsStudents) {
        teamsStudents.forEach((teamName, loginsList) -> {
            TeamBo newTeam = teamService.generateNewTeam(teamName, course);
            
            List<UserBo> existingStudents = userRepository.findAllByLogins(loginsList);
            
            loginsList.forEach(login -> {
                UserBo existingStudent = existingStudents.stream()
                    .filter(student -> login.matches(student.getLogin()))
                    .findFirst()
                    .orElse(null);

                //student already exists
                if (existingStudent != null && course.getStudents().contains(existingStudent) && !IsInATeamInCourse(existingStudent, course)) {
                    addTeamToStudent(existingStudent, newTeam);
                    addStudentToTeam(newTeam, existingStudent);
                }
            });

            if (newTeam.getMembers().isEmpty()) {
                teamRepository.delete(newTeam);
            } else {
                createTeamProjectsForAllCourseAssignment(newTeam, course);
                teamRepository.save(newTeam);
            }
        });
    }
    
    private boolean IsInATeamInCourse(UserBo student, CourseBo course) {
        return course.getTeams().stream()
                .anyMatch(team -> team.getMembers().contains(student));
    }
    
    private void addTeamToStudent(UserBo student, TeamBo team) {
        List<TeamBo> teams = student.getTeams();
        teams.add(team);
        student.setTeams(teams);
    }
    
    private void addStudentToTeam(TeamBo team, UserBo student) {
        List<UserBo> members = team.getMembers();
        members.add(student);
        team.setMembers(members);
        
        systemManager.addUserToGroup(student.getLogin(), team.getGroupName());
    }
    
    private void createTeamProjectsForAllCourseAssignment(TeamBo team, CourseBo course) {
        List<ProjectAssignmentBo> teamCourseAssignments = course.getProjectAssignments().stream()
                .filter(assignment -> assignment.isTeam())
                .collect(Collectors.toList());
        
        teamCourseAssignments.forEach(assignment -> {
            projectService.createTeamProject(team, assignment);
        });
    }

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

        systemAdapter.createAssignment(assignment);

        if (course.getStudents().isEmpty()) {
            return;
        }

        if (isTeam) {
            //create project for each team
            course.getTeams().forEach(team -> projectService.createTeamProject(team, assignment));
        } else {
            //create project for each student
            course.getStudents().forEach(student -> projectService.createProject(student, assignment));
        }
    }

    public void removeAssignmentFromCourse(@NotNull CourseBo course, @NotNull ProjectAssignmentBo assignment) {
        assignment.getProjects().forEach(projectService::removeProject);
        systemAdapter.deleteAssignment(assignment);
        assignmentRepository.delete(assignment);
    }
}
