package vut.fit.gja2023.app;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import vut.fit.gja2023.app.entity.TeamBo;
import vut.fit.gja2023.app.service.TeamService;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import vut.fit.gja2023.app.repository.TeamRepository;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
public class TeamServiceTests {
    
    private static final String TEAM_NAME = "Žaňkův tým";
    
    private TeamService teamService;
    private TeamRepository teamRepository;
    
    private TeamBo teamWithName;
    
    @BeforeAll
    void setup() {
        teamRepository = mock(TeamRepository.class);
        teamService = new TeamService(teamRepository);
        
        teamWithName = new TeamBo();
        teamWithName.setName(TEAM_NAME);
    }
    
    @Test
    void testSaveTeamResult() {
        assertDoesNotThrow(() -> {
            teamService.saveTeam(TEAM_NAME);
        });
    }
    
    @Test
    void testGetTeam() {
        when(teamRepository.findByTeamName(TEAM_NAME)).thenReturn(Optional.ofNullable(teamWithName));
        
        TeamBo team = teamService.getTeamByName(TEAM_NAME);
        
        assertNotNull(team);
        assertEquals(TEAM_NAME, team.getName());
    }
    
    @Test
    void testGetNullTeam() {
        when(teamRepository.findByTeamName(TEAM_NAME)).thenReturn(Optional.ofNullable(null));
        
        assertNull(teamService.getTeamByName(TEAM_NAME));
    }
}
