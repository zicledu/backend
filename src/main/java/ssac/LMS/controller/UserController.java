package ssac.LMS.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import ssac.LMS.dto.JoinRequestDto;
import ssac.LMS.dto.LoginRequestDto;
import ssac.LMS.dto.LoginResponseDto;
import ssac.LMS.service.AuthService;

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
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {

        LoginResponseDto loginResult = authService.login(loginRequestDto);

//        log.info("loginResult={}", loginResult.);


        return ResponseEntity.status(HttpStatus.OK).body(loginResult);
    }
}
