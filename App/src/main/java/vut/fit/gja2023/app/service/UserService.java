package vut.fit.gja2023.app.service;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.CourseBo;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.enums.UserRole;
import vut.fit.gja2023.app.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final TeamService teamService;

    private final UserRepository userRepository;

    private final SystemAdapter systemAdapter;
    private final SystemManagerService systemManager;

    @Transactional
    public UserBo saveUser(String name, String login) {
        UserBo user = new UserBo();
        user.setName(name);
        user.setLogin(login);
        return userRepository.save(user);
    }

    @Transactional
    public List<UserBo> getUsersByName(String name) {
        return userRepository.findByUserName(name);
    }
    
    @Transactional
    public UserBo getUserByLogin(String login) {
        Optional<UserBo> queryResult = userRepository.findByUserLogin(login);
        return queryResult.orElse(null);
    }

    /**
     * Deletes user and his firewall rules from DB and OS, teams (and its projects) that contained
     * only this user arte also removed from DB and OS.
     * @apiNote Assumes that that student is not in any courses (nor have any projects) !!!
     * @param student student which will be removed from the system.
     */
    public void deleteStudent(UserBo student) {
        //delete teams that contains only this student
        student.getTeams().stream()
            .filter(team -> team.getMembers().size() == 1)
            .forEach(teamService::deleteTeam);

        //remove all firewall rules set by this student
        student.getFirewallRules()
            .forEach(rule -> systemManager.removeFirewallRule(rule.getAddress()));

        systemManager.deleteUser(student.getLogin());
        userRepository.delete(student);
    }

    public UserBo generateUser(String login, String name, UserRole role) {
        var user = new UserBo();
        user.setLogin(login);
        user.setName(name);
        user.setRole(role);
        user.setStudiedCourses(new ArrayList<>());
        user.setCoordinatedCourses(new ArrayList<>());
        user.setGuaranteedCourses(new ArrayList<>());
        user.setFirewallRules(new ArrayList<>());
        user.setProjects(new ArrayList<>());

        var password = "alohomora";
        //TODO
        // - generate password
        // - send email

        systemManager.createUser(user.getLogin(), password);
        return user;
    }
}
