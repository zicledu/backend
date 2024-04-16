package ssac.LMS.dto;

import lombok.Data;

@Data
public class JoinRequestDto {
    private String email;
    private String userName;
    private String telephone;
    private String password;
    private String role;
}
