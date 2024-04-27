package ssac.LMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyLectureResponseDto {
    private Long courseId;
    private String title;
    private Integer lectureOrder;
    private String durationMinutes;
    private String videoPath720;
    private String videoPath1080;
    private String videoPathOriginal;
    private String thumbnailPath;

}
