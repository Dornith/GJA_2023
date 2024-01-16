package vut.fit.gja2023.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.CourseBo;
import vut.fit.gja2023.app.entity.ProjectAssignmentBo;
import vut.fit.gja2023.app.entity.ProjectBo;
import vut.fit.gja2023.app.entity.TeamBo;
import vut.fit.gja2023.app.enums.DirectoryModeEnum;

import java.io.Console;

@Service
@RequiredArgsConstructor
public class SystemAdapter {
    private final SystemManagerService systemManager;

    @Value("${systemManager.path.projects}")
    private String PROJECT_ROOT_PATH;

    public void createCourse(CourseBo course) {
        var path = getCoursePath(course);
        systemManager.createDirectory(path, DirectoryModeEnum.PUBLIC, course.getGuarantor().getLogin());
    }

    public void createAssignment(ProjectAssignmentBo assignment) {
        var path = getAssignmentPath(assignment);
        systemManager.createDirectory(path, DirectoryModeEnum.PUBLIC, assignment.getCourse().getGuarantor().getLogin());
    }

    public void createProject(ProjectBo project) {
        var path = getProjectPath(project);
        systemManager.createDirectory(path, DirectoryModeEnum.PRIVATE, project.getAuthor().getLogin());
    }

    public void createTeamProject(ProjectBo project) {
        var path = getProjectPath(project);
        systemManager.createDirectory(path, DirectoryModeEnum.PROJECT, project.getTeam().getGroupName());
    }

    public void createTeam(TeamBo team) {
        systemManager.createGroup(team.getGroupName());
        team.getMembers().forEach(member -> systemManager.addUserToGroup(member.getLogin(), team.getGroupName()));
    }

    public String getProjectPath(ProjectBo project) {
        var assignment = project.getAssignment();
        // ROOT_PATH/COURSE/ASSIGNMENT/TEAM OR USER
        return getAssignmentPath(assignment) + "/" + (assignment.isTeam() ? project.getTeam().getGroupName() : project.getAuthor().getLogin());
    }

    public String getAssignmentPath(ProjectAssignmentBo assignment) {
        // ROOT_PATH/COURSE/ASSIGNMENT/
        return getCoursePath(assignment.getCourse()) + "/" + assignment.getName();
    }

    public String getCoursePath(CourseBo course) {
        return PROJECT_ROOT_PATH + "/" + course.getAbb();
    }

    public void deleteProject(ProjectBo project) {
        var path = getProjectPath(project);
        systemManager.deleteDirectory(path);
    }

    public void deleteTeam(TeamBo team) {
        team.getProjects().forEach(this::deleteProject);
        systemManager.deleteGroup(team.getGroupName());
    }
}
