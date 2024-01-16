package vut.fit.gja2023.app.service;

import jakarta.validation.constraints.NotNull;
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

    public void createCourse(@NotNull CourseBo course) {
        var path = getCoursePath(course);
        systemManager.createDirectory(path, DirectoryModeEnum.PUBLIC, course.getGuarantor().getLogin());
    }

    public void createAssignment(@NotNull ProjectAssignmentBo assignment) {
        var path = getAssignmentPath(assignment);
        systemManager.createDirectory(path, DirectoryModeEnum.PUBLIC, assignment.getCourse().getGuarantor().getLogin());
    }

    public void createProject(@NotNull ProjectBo project) {
        var path = getProjectPath(project);
        systemManager.createDirectory(path, DirectoryModeEnum.PRIVATE, project.getAuthor().getLogin());
    }

    public void createTeamProject(@NotNull ProjectBo project) {
        var path = getProjectPath(project);
        systemManager.createDirectory(path, DirectoryModeEnum.PROJECT, project.getTeam().getGroupName());
    }

    public void createTeam(@NotNull TeamBo team) {
        systemManager.createGroup(team.getGroupName());
        team.getMembers().forEach(member -> systemManager.addUserToGroup(member.getLogin(), team.getGroupName()));
    }

    public String getProjectPath(@NotNull ProjectBo project) {
        var assignment = project.getAssignment();
        // ROOT_PATH/COURSE/ASSIGNMENT/TEAM OR USER
        return getAssignmentPath(assignment) + "/" + (assignment.isTeam() ? project.getTeam().getGroupName() : project.getAuthor().getLogin());
    }

    public String getAssignmentPath(@NotNull ProjectAssignmentBo assignment) {
        // ROOT_PATH/COURSE/ASSIGNMENT/
        return getCoursePath(assignment.getCourse()) + "/" + assignment.getName();
    }

    public String getCoursePath(@NotNull CourseBo course) {
        return "projects/" + course.getAbb();
    }

    public void deleteProject(@NotNull ProjectBo project) {
        var path = getProjectPath(project);
        systemManager.deleteDirectory(path);
    }

    public void deleteTeam(@NotNull TeamBo team) {
        team.getProjects().forEach(this::deleteProject);
        systemManager.deleteGroup(team.getGroupName());
    }

    public void deleteAssignment(@NotNull ProjectAssignmentBo assignment) {
        var path = getAssignmentPath(assignment);
        systemManager.deleteDirectory(path);
    }
}
