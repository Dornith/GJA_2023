package vut.fit.gja2023.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.CourseBo;
import vut.fit.gja2023.app.entity.TeamBo;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.repository.TeamRepository;
import vut.fit.gja2023.app.util.OSNameParser;

import java.util.ArrayList;

/**
 * A service for working with teams.
 */
@Service
@RequiredArgsConstructor
public class TeamService {
    private static final int GROUP_NAME_MAX_LENGTH = 32;
    
    private final TeamRepository teamRepository;
    private final SystemAdapter systemAdapter;
    private final SystemManagerService systemManager;

    /**
     * Saves a new team to the database.
     * @param name The name of the team.
     * @return A business object representing the team.
     */
    @Transactional
    public TeamBo saveTeam(String name) {
        TeamBo team = new TeamBo();
        team.setName(name);
        team.setGroupName(convertNameToGroupId(name));
        
        return teamRepository.save(team);
    }

    /**
     * Retrieves a team with a specified name from the database.
     * @param name The name of the team.
     * @return A bussiness object representing the team.
     */
    @Transactional
    public TeamBo getTeamByName(String name) {
        return teamRepository.findByTeamName(name).orElse(null);
    }
    
    /**
     * Converts the team name to a unique name that can be used as a group name in linux systems.
     * @param name The team name.
     * @return A new group name.
     */
    private String convertNameToGroupId(String name) {
        return OSNameParser.toOS(name, GROUP_NAME_MAX_LENGTH);
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
    
    public TeamBo generateNewTeam(String name, CourseBo course) {
        TeamBo newTeam = new TeamBo();
        newTeam.setName(name);
        newTeam.setGroupName(OSNameParser.toOS(name, GROUP_NAME_MAX_LENGTH));
        newTeam.setCourse(course);
        newTeam.setMembers(new ArrayList<>());
        teamRepository.save(newTeam);
        systemManager.createGroup(newTeam.getGroupName());
        return newTeam;
    }
}
