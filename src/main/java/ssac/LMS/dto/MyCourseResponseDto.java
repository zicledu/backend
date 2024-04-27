package ssac.LMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCourseResponseDto {

    private Long id;
    private String courseTitle;
    private String instructor;
    private LocalDateTime enrolledAt;
    private String thumbnailPath;

}
