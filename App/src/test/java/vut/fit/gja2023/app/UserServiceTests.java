package vut.fit.gja2023.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import vut.fit.gja2023.app.service.SystemAdapter;
import vut.fit.gja2023.app.service.SystemManagerService;
import vut.fit.gja2023.app.service.TeamService;
import vut.fit.gja2023.app.service.UserService;
import vut.fit.gja2023.app.entity.UserBo;
import org.junit.jupiter.api.TestInstance;
import vut.fit.gja2023.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTests {
    
    private static final String USERS_NAME = "Pepa Potopa";
    private static final String USER_ONE_LOGIN = "xpotop00";
    private static final String USER_TWO_LOGIN = "xpotop01";
    
    private UserRepository userRepository;
    private UserService userService;
    
    private UserBo userOne;
    private UserBo userTwo;
    
    @BeforeAll
    void setup() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(
            mock(TeamService.class),
            userRepository,
            mock(SystemAdapter.class),
            mock(SystemManagerService.class)
        );
        
        userOne = new UserBo();
        userOne.setName(USERS_NAME);
        userOne.setLogin(USER_ONE_LOGIN);
        
        userTwo = new UserBo();
        userTwo.setName(USERS_NAME);
        userTwo.setLogin(USER_TWO_LOGIN);
    }
    
    @Test
    void testSaveUserResult() {
        assertDoesNotThrow(() -> {
            userService.saveUser(USERS_NAME, USER_ONE_LOGIN);
        });
    }
    
    @Test
    void testGetUsersByName() {
        List<UserBo> users = new ArrayList<>();
        users.add(userOne);
        users.add(userTwo);

        when(userRepository.findByUserName(USERS_NAME)).thenReturn(users);
        
        List<UserBo> res = userService.getUsersByName(USERS_NAME);
        assertEquals(users, res);
        assertEquals(2, res.size());
        assertEquals(res.get(0).getName(), res.get(1).getName());
        assertNotEquals(res.get(0).getLogin(), res.get(1).getLogin());
    }
    
    @Test
    void testGetUserByLogin() {
        when(userRepository.findByUserLogin(USER_ONE_LOGIN)).thenReturn(Optional.ofNullable(userOne));
        
        UserBo user = userService.getUserByLogin(USER_ONE_LOGIN);
        
        assertNotNull(user);
        assertEquals(userOne, user);
    }
    
    @Test
    void testGetNullUserByLogin() {
        when(userRepository.findByUserLogin(USER_ONE_LOGIN)).thenReturn(Optional.ofNullable(null));
        
        UserBo user = userService.getUserByLogin(USER_ONE_LOGIN);
        
        assertNull(user);
    }
}
