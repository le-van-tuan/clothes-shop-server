package vn.triumphstudio.clothesshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.triumphstudio.clothesshop.domain.request.LoginRequest;
import vn.triumphstudio.clothesshop.domain.request.SignUpRequest;
import vn.triumphstudio.clothesshop.domain.response.ApiResponse;
import vn.triumphstudio.clothesshop.domain.response.AuthResponse;
import vn.triumphstudio.clothesshop.security.JwtTokenProvider;
import vn.triumphstudio.clothesshop.security.UserPrincipal;
import vn.triumphstudio.clothesshop.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public AuthResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String accessToken = jwtTokenProvider.generateAccessToken(userPrincipal);

            return new AuthResponse(accessToken, userPrincipal.getRole(), userPrincipal.getName());
        } catch (DisabledException e) {
            throw new BadCredentialsException("User has been disabled.");
        }
    }

    @PostMapping("/sign-up")
    public ApiResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        this.userService.signUp(signUpRequest);
        return new ApiResponse(true, "User has been registered successfully");
    }
}
