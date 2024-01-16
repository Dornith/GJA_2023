package vut.fit.gja2023.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.ProjectAssignmentBo;
import vut.fit.gja2023.app.entity.ProjectBo;
import vut.fit.gja2023.app.entity.TeamBo;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.repository.ProjectRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final SystemAdapter systemAdapter;

    public void createProject(UserBo student, ProjectAssignmentBo assignment) {
        var project = new ProjectBo();
        project.setAssignment(assignment);
        project.setAuthor(student);
        project.setSubmitted(false);
        project.setSubmittedDate(new Date());

        systemAdapter.createProject(project);
        projectRepository.save(project);
    }

    public void createTeamProject(TeamBo team, ProjectAssignmentBo assignment) {
        var project = new ProjectBo();
        project.setAssignment(assignment);
        project.setTeam(team);
        project.setSubmitted(false);
        project.setSubmittedDate(new Date());

        systemAdapter.createTeamProject(project);
        projectRepository.save(project);
    }

    public void removeProject(ProjectBo project) {
        systemAdapter.deleteProject(project);
        projectRepository.deleteById(project.getId());
    }
}
