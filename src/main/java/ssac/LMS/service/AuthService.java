package ssac.LMS.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import ssac.LMS.dto.*;

public interface AuthService {

    ResponseEntity<String> join(JoinRequestDto joinRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    LoginResponseDto refresh(RefreshDto refreshDto);
}
