package pl.devopsijwt.security.api;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.devopsijwt.security.config.JwtTokenUtil;
import pl.devopsijwt.security.domain.dto.AuthRequest;
import pl.devopsijwt.security.domain.dto.CreateUserRequest;
import pl.devopsijwt.security.domain.dto.CreateUserResponse;
import pl.devopsijwt.security.domain.dto.JwtResponse;
import pl.devopsijwt.security.domain.dto.RefreshTokenRequest;
import pl.devopsijwt.security.domain.dto.TokenRefreshResponse;
import pl.devopsijwt.security.domain.model.RefreshToken;
import pl.devopsijwt.security.domain.model.User;
import pl.devopsijwt.security.domain.service.RefreshTokenService;
import pl.devopsijwt.security.domain.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            User userDetails = (User) authenticate.getPrincipal();

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return ResponseEntity.ok()
                .body(new JwtResponse(jwtTokenUtil.generateAccessToken(userDetails), refreshToken.getToken()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return refreshTokenService.refreshToken(request);
    }

    @PostMapping("/register")
    public CreateUserResponse register(@RequestBody @Valid CreateUserRequest request) {

        return userService.create(request);
    }

}
