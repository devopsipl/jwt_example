package pl.devopsijwt.security.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.devopsijwt.security.domain.dto.CreateUserRequest;
import pl.devopsijwt.security.domain.dto.CreateUserResponse;
import pl.devopsijwt.security.domain.mapper.UserCreateMapper;
import pl.devopsijwt.security.domain.mapper.UserViewMapper;
import pl.devopsijwt.security.domain.model.User;
import pl.devopsijwt.security.infrastructure.reopsitory.UserRepo;

import javax.validation.ValidationException;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final UserCreateMapper userCreateMapper;
    private final UserViewMapper userViewMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CreateUserResponse create(CreateUserRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException("Username exists!");
        }
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new ValidationException("Passwords don't match!");
        }

        User user = userCreateMapper.create(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepo.save(user);

        return userViewMapper.toUserView(user);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo
            .findByEmail(username)
            .orElseThrow(
                () -> new UsernameNotFoundException(format("User with username - %s, not found", username))
            );
    }

}
