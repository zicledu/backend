package ssac.LMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstructorResponseDto {

    private String userId;
    private String userName;
    private String email;
    private int enrollmentCount;

}
