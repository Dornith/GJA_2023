package vut.fit.gja2023.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import vut.fit.gja2023.app.enums.UserRole;
import vut.fit.gja2023.app.service.UserDetailsServiceImpl;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] teacherPlus = { UserRole.ADMIN.getValue(), UserRole.TEACHER.getValue() };

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> {
                authorize
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/courses/*/students").hasAnyRole(teacherPlus)
                    .requestMatchers("/courses/*/students/upload").hasAnyRole(teacherPlus)
                    .requestMatchers("/courses/*/assignments/*").hasAnyRole(teacherPlus)
                    .requestMatchers("/teams").hasAnyRole(UserRole.STUDENT.getValue())
                    .requestMatchers("/projects").hasAnyRole(UserRole.STUDENT.getValue())
                    .anyRequest().authenticated();
            })
            .formLogin(form -> {
                form.loginPage("/login")
                    .defaultSuccessUrl("/")
                    .successForwardUrl("/")
                    .permitAll();
            })
            .logout(logout -> {
                logout
                    .logoutUrl("/perform-logout")
                    .logoutSuccessUrl("/login")
                    .permitAll();
            })
            .sessionManagement(config -> {
                config.maximumSessions(1);
                config.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
            });
        return http.build();
    }
}
