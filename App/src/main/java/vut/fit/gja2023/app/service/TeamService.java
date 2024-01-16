package vut.fit.gja2023.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.TeamBo;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.repository.TeamRepository;
import vut.fit.gja2023.app.util.OSNameParser;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final SystemAdapter systemAdapter;
    private final SystemManagerService systemManager;

    @Transactional
    public TeamBo saveTeam(String name) {
        TeamBo team = new TeamBo();
        team.setName(name);
        team.setGroupName(convertNameToGroupId(name));
        
        return teamRepository.save(team);
    }

    @Transactional
    public TeamBo getTeamByName(String name) {
        return teamRepository.findByTeamName(name).orElse(null);
    }
    
    private String convertNameToGroupId(String name) {
        return OSNameParser.toOS(name);
    }

    public void deleteTeam(TeamBo team) {
        systemAdapter.deleteTeam(team);
        teamRepository.delete(team);
    }

    public void removeStudentFromTeam(TeamBo team, UserBo student) {
        team.getMembers().remove(student);

        if (team.getMembers().isEmpty()) {
            deleteTeam(team);
        } else {
            teamRepository.save(team);
            systemManager.removeUserFromGroup(student.getLogin(), team.getGroupName());
        }
    }
}
