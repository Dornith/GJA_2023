package vut.fit.gja2023.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.repository.UserRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userResult = userRepository.findByUserLogin(username);
        if (userResult.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        var user = userResult.get();
        var au = new User(
            user.getLogin(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getValue()))
        );

        return au;
    }
}
