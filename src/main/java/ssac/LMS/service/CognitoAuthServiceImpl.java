package ssac.LMS.service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;
import ssac.LMS.domain.Role;
import ssac.LMS.domain.User;
import ssac.LMS.dto.JoinRequestDto;
import ssac.LMS.dto.LoginRequestDto;
import ssac.LMS.dto.LoginResponseDto;
import ssac.LMS.dto.RefreshDto;
import ssac.LMS.exception.CustomUsernameExistsException;
import ssac.LMS.exception.InvalidCredentialsException;
import ssac.LMS.repository.UserRepository;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
@Slf4j
public class CognitoAuthServiceImpl implements AuthService{

    @Value("${spring.security.oauth2.client.registration.cognito.client-id}")
    private String CLIENT_ID;
    private final UserRepository userRepository;
    private final CognitoIdentityProviderClient cognitoClient;

    @Override
    @Transactional
    public ResponseEntity<String> join(JoinRequestDto joinRequestDto) {

        log.info("Starting user sign up for email: {}", joinRequestDto.getEmail());

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
            SignUpResponse signUpResponse = cognitoClient.signUp(signUpRequest);
            String id = signUpResponse.userSub();

            log.info("Cognito sign up successful for user id: {}", id);

            User user = new User();
            user.setUserId(id);
            user.setUserName(joinRequestDto.getUserName());
            user.setEmail(joinRequestDto.getEmail());
            user.setTelephone(joinRequestDto.getTelephone());

            String inputRole = joinRequestDto.getRole();
            Role role = Role.fromValue(inputRole);
            user.setRole(role);

            user.setCreatedAt(LocalDateTime.now());
            user.setIsDeleted(false);

            userRepository.save(user);

            log.info("saveUser={}", user.getUserName());

            return ResponseEntity.ok("User signed up successfully!");

        } catch (UsernameExistsException e) {
            throw new CustomUsernameExistsException("아이디가 이미 사용 중입니다.");
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

        try {
            InitiateAuthResponse authResponse = cognitoClient.initiateAuth(authRequest);
            AuthenticationResultType authenticationResultType = authResponse.authenticationResult();

            // token 생성
            LoginResponseDto loginResponseDto = buildLoginReponse(authenticationResultType);

            return loginResponseDto;
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("잘못된 이메일 또는 비밀번호입니다.");
        } catch (Exception e) {
            log.error("Login error", e);
            throw new InvalidCredentialsException("로그인 중 오류가 발생했습니다.");
        }
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
            InitiateAuthResponse authResponse = cognitoClient.initiateAuth(authRequest);
            AuthenticationResultType authenticationResultType = authResponse.authenticationResult();

            // token 생성
            LoginResponseDto loginResponseDto = buildLoginReponse(authenticationResultType);

            return loginResponseDto;
        }catch (Exception e) {
            log.info("error={}", e);
            return null;
        }
    }

    private LoginResponseDto buildLoginReponse(AuthenticationResultType authenticationResultType) throws ParseException {
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
        return new LoginResponseDto(id, name, email, role, koreaTime, tokenDto);
    }

}
