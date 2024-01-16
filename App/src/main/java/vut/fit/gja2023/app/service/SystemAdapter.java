package vut.fit.gja2023.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
        return PROJECT_ROOT_PATH + "/" +
            assignment.getCourse().getAbb() + "/" +
            assignment.getName() + "/" +
            (assignment.isTeam() ? project.getTeam().getGroupName() : project.getAuthor().getLogin());
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
