package vut.fit.gja2023.app.service;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.CourseBo;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.enums.UserRole;
import vut.fit.gja2023.app.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A service for working with users.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final TeamService teamService;

    private final UserRepository userRepository;

    private final SystemAdapter systemAdapter;
    private final SystemManagerService systemManager;
    private final PasswordEncoder passwordEncoder;

    private static Predicate<CourseBo> isNotIn(List<CourseBo> courses) {
        return cA -> courses.stream().noneMatch(cB -> cB.getId().equals(cA.getId()));
    }

    /**
     * Saves a new user to the database.
     * 
     * @param name The name of the user.
     * @param login The unique login of the user.
     * @return A business object representing the user.
     */
    @Transactional
    public UserBo saveUser(String name, String login) {
        UserBo user = new UserBo();
        user.setName(name);
        user.setLogin(login);
        return userRepository.save(user);
    }

    /**
     * Retrieves users with a specified name from the database.
     * @param name The name of the users.
     * @return A list of bussines objects representing users.
     */
    @Transactional
    public List<UserBo> getUsersByName(String name) {
        return userRepository.findByUserName(name);
    }
    
    /**
     * Retrieves a user with a specified login from the database.
     * @param login The user's login.
     * @return A bussines object representing the user.
     */
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
    public void deleteStudent(@NotNull UserBo student) {
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

    public UserBo generateUser(@NotNull String login, @NotNull String name, @NotNull UserRole role) {
        var password = "alohomora";
        //TODO
        // - generate password
        // - send email

        var user = new UserBo();
        user.setLogin(login);
        user.setName(name);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(password));
        user.setStudiedCourses(new ArrayList<>());
        user.setCoordinatedCourses(new ArrayList<>());
        user.setGuaranteedCourses(new ArrayList<>());
        user.setFirewallRules(new ArrayList<>());
        user.setProjects(new ArrayList<>());
        userRepository.save(user);

        systemManager.createUser(user.getLogin(), password);
        return user;
    }

    public Optional<UserBo> getLoggedInUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            var principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return userRepository.findByUserLogin(((UserDetails)principal).getUsername());
            }
        }
        return Optional.empty();
    }

    public List<CourseBo> getTeachingCourses(UserBo teacher) {
        var guaranteed = teacher.getGuaranteedCourses();
        var coordinated = teacher.getCoordinatedCourses();

        return Stream.concat(
            guaranteed.stream(),
            coordinated.stream().filter(isNotIn(guaranteed))
        ).toList();
    }

    public boolean isLoggedIn() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            var principal = authentication.getPrincipal();
            return principal instanceof UserDetails;
        }
        return false;
    }
}
