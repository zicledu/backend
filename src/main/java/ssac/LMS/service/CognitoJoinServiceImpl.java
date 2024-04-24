package ssac.LMS.service;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;
import ssac.LMS.domain.Role;
import ssac.LMS.domain.User;
import ssac.LMS.dto.JoinRequestDto;
import ssac.LMS.dto.LoginRequestDto;
import ssac.LMS.dto.LoginResponseDto;
import ssac.LMS.dto.RefreshDto;
import ssac.LMS.repository.UserRepository;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
@Slf4j
public class CognitoJoinServiceImpl implements AuthService{

    @Value("${spring.aws.region}")
    private String AWS_REGION;

    @Value("${spring.security.oauth2.client.registration.cognito.client-id}")
    private String CLIENT_ID;

    @Value("${cognito.user-pool-id}")
    private String userPoolId;

    private final UserRepository userRepository;

    public CognitoIdentityProviderClient getCognitoClient() {
        CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.of(AWS_REGION))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        return cognitoClient;
    }

    @Override
    public ResponseEntity<String> join(JoinRequestDto joinRequestDto) {

        // 클라이언트 자격 증명 생성
        CognitoIdentityProviderClient cognitoClient = getCognitoClient();

        log.info("cognitoClient={}", cognitoClient);

        // Cognito 회원 가입 진행
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId(CLIENT_ID)
                .username(joinRequestDto.getEmail())
                .password(joinRequestDto.getPassword())
                .userAttributes(
                        AttributeType.builder().name("custom:role").value(joinRequestDto.getRole()).build(),
                        AttributeType.builder().name("name").value(joinRequestDto.getUserName()).build()
                )
                .build();
        try {

            cognitoClient.signUp(signUpRequest);

            User user = new User();
            user.setUserName(joinRequestDto.getUserName());
            user.setEmail(joinRequestDto.getEmail());
            user.setTelephone(joinRequestDto.getTelephone());
            String inputRole = joinRequestDto.getRole();
            Role role = Role.fromValue(inputRole);
            user.setRole(role);
            user.setCreatedAt(LocalDateTime.now());
            user.setIsDeleted(false);

            log.info("createdUser={}", user);

            userRepository.save(user);

//            userRepository.existsByTelephone();

            log.info("saveUser={}", user.getUserName());

            return ResponseEntity.ok("User signed up successfully!");

        } catch (Exception e) {

            log.error("error={}", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed");

        }

    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) throws ParseException {

        InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                .authFlow("USER_PASSWORD_AUTH")
                .clientId(CLIENT_ID)
                .authParameters(Map.of("USERNAME", loginRequestDto.getEmail(), "PASSWORD", loginRequestDto.getPassword()))
                .build();

        CognitoIdentityProviderClient cognitoClient = getCognitoClient();
        InitiateAuthResponse authResponse = cognitoClient.initiateAuth(authRequest);
        AuthenticationResultType authenticationResultType = authResponse.authenticationResult();

        String accessToken = authenticationResultType.accessToken();
        String idToken = authenticationResultType.idToken();
        String refreshToken = authenticationResultType.refreshToken();

        String email = JWTParser.parse(idToken).getJWTClaimsSet().getClaim("email").toString();
        String name = JWTParser.parse(idToken).getJWTClaimsSet().getClaim("name").toString();
        String id = JWTParser.parse(idToken).getJWTClaimsSet().getClaim("cognito:username").toString();
        String role = JWTParser.parse(idToken).getJWTClaimsSet().getClaim("custom:role").toString();

        // JWTClaimsSet에서 만료 시간을 가져옴

        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");

        // JWTClaimsSetParser를 사용하여 JWTClaimsSet를 파싱
        JWTClaimsSet jwtClaimsSet = JWTParser.parse(idToken).getJWTClaimsSet();

        // JWTClaimsSet에서 만료 시간을 가져옴
        LocalDateTime expirationTime = jwtClaimsSet.getExpirationTime().toInstant()
                .atZone(ZoneId.of("UTC")).toLocalDateTime();

        // 만료 시간을 한국 표준 시간대로 변환
        LocalDateTime koreaTime = expirationTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(timeZone.toZoneId())
                .toLocalDateTime();

        LoginResponseDto.TokenDto tokenDto= new LoginResponseDto.TokenDto(accessToken, idToken, refreshToken);
        LoginResponseDto loginResponseDto = new LoginResponseDto(id, name, email, role, koreaTime, tokenDto);

        return loginResponseDto;
    }

    @Override
    public LoginResponseDto refresh(RefreshDto refreshDto) {
        log.info("refreshDto={}", refreshDto);
        InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                .authFlow("REFRESH_TOKEN")
                .clientId(CLIENT_ID)
                .authParameters(Map.of("REFRESH_TOKEN", refreshDto.getRefreshToken()))
                .build();

        try {
            CognitoIdentityProviderClient cognitoClient = getCognitoClient();
            InitiateAuthResponse authResponse = cognitoClient.initiateAuth(authRequest);

            AuthenticationResultType authenticationResultType = authResponse.authenticationResult();

            String accessToken = authenticationResultType.accessToken();
            String idToken = authenticationResultType.idToken();
            String refreshToken = authenticationResultType.refreshToken();

            String email = JWTParser.parse(idToken).getJWTClaimsSet().getClaim("email").toString();
            String name = JWTParser.parse(idToken).getJWTClaimsSet().getClaim("name").toString();
            String id = JWTParser.parse(idToken).getJWTClaimsSet().getClaim("cognito:username").toString();
            String role = JWTParser.parse(idToken).getJWTClaimsSet().getClaim("custom:role").toString();

            TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");

            // JWTClaimsSetParser를 사용하여 JWTClaimsSet를 파싱
            JWTClaimsSet jwtClaimsSet = JWTParser.parse(idToken).getJWTClaimsSet();

            // JWTClaimsSet에서 만료 시간을 가져옴
            LocalDateTime expirationTime = jwtClaimsSet.getExpirationTime().toInstant()
                    .atZone(ZoneId.of("UTC")).toLocalDateTime();

            // 만료 시간을 한국 표준 시간대로 변환
            LocalDateTime koreaTime = expirationTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(timeZone.toZoneId())
                    .toLocalDateTime();

            LoginResponseDto.TokenDto tokenDto= new LoginResponseDto.TokenDto(accessToken, idToken, refreshToken);
            LoginResponseDto loginResponseDto = new LoginResponseDto(id, name, email, role, koreaTime, tokenDto);

            return loginResponseDto;
        }catch (Exception e) {
            log.info("error={}", e);
            return null;
        }
    }

}
