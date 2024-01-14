package vut.fit.gja2023.app.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
}
