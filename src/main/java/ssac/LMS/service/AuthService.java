package ssac.LMS.service;

import org.springframework.http.ResponseEntity;
import ssac.LMS.dto.JoinRequestDto;
import ssac.LMS.dto.LoginRequestDto;
import ssac.LMS.dto.LoginResponseDto;

public interface AuthService {

    ResponseEntity<String> join(JoinRequestDto joinRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
