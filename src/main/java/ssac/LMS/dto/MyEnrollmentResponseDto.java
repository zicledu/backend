package ssac.LMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyEnrollmentResponseDto {

    private String userId;
    private Long courseId;
    private Long enrollment_Id;
    private String title;
    private String durationMinutes;
    private String videoPath720;
    private String videoPath1080;
    private String videoPathOriginal;
    private String thumbnailPath;

}

