package vut.fit.gja2023.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.TeamBo;
import vut.fit.gja2023.app.repository.TeamRepository;

@Service
@RequiredArgsConstructor
public class TeamService {

    private static final String REGEX_FORBIDDEN_SYMBOLS = "[^a-zA-Z\\d]";
    
    private final TeamRepository teamRepository;

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
        String newName = name.replaceAll(REGEX_FORBIDDEN_SYMBOLS, "");
        String uid = java.util.UUID.randomUUID().toString();

        return newName + uid;
    }
}
