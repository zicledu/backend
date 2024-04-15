package ssac.LMS.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ssac.LMS.dto.*;
import ssac.LMS.service.AuthService;
import ssac.LMS.service.CognitoJoinServiceImpl;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthService authService;

    @Value("${spring.security.oauth2.client.registration.cognito.client-id}")
    private String clientId;

    @Value("${spring.aws.region}")
    private String awsRegion;

    @PostMapping("/join")
    public ResponseEntity<?> signUp(@RequestBody JoinRequestDto joinRequestDto) {

        ResponseEntity<String> signUpResult = authService.join(joinRequestDto);
        return signUpResult;

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {

        LoginResponseDto loginResult = authService.login(loginRequestDto);

        log.info("loginResult={}", loginResult);


        return ResponseEntity.status(HttpStatus.OK).body(loginResult);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody RefreshDto refreshDto) {
        LoginResponseDto refresh = authService.refresh(refreshDto);
        return ResponseEntity.status(HttpStatus.OK).body(refresh);
    }

}
