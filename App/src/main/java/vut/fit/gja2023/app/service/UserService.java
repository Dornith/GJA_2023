package vut.fit.gja2023.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserBo saveUser(String name) {
        UserBo user = new UserBo();
        user.setName(name);
        return userRepository.save(user);
    }

    public UserBo getUser(String name) {
        Optional<UserBo> queryResult = userRepository.findByUserName(name);
        return queryResult.orElse(null);
    }
}
