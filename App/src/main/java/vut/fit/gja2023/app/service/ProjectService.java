package vut.fit.gja2023.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.ProjectAssignmentBo;
import vut.fit.gja2023.app.entity.ProjectBo;
import vut.fit.gja2023.app.entity.TeamBo;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final SystemAdapter systemAdapter;

    @Transactional
    public ProjectBo createProject(UserBo student, ProjectAssignmentBo assignment) {
        var project = new ProjectBo();
        project.setAssignment(assignment);
        project.setAuthor(student);

        systemAdapter.createProject(project);
        projectRepository.save(project);
        return project;
    }

    @Transactional
    public ProjectBo createTeamProject(TeamBo team, ProjectAssignmentBo assignment) {
        var project = new ProjectBo();
        project.setAssignment(assignment);
        project.setTeam(team);

        systemAdapter.createTeamProject(project);
        projectRepository.save(project);
        return project;
    }

    @Transactional
    public void removeProject(ProjectBo project) {
        systemAdapter.deleteProject(project);
        projectRepository.delete(project);
    }
}
