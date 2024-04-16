package ssac.LMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class LoginResponseDto {

    private String userId;
    private String userName;
    private String email;
    private LocalDateTime expiredDate;
    private TokenDto tokenDto;

    @Data
    @AllArgsConstructor
    public static class TokenDto {
        private String accessToken;
        private String idToken;
        private String refreshToken;

    }
}
