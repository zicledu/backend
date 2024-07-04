package ssac.LMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailCurriculumResponseDto {
    private Long id;
    private String title;
    private String runTime;
    private int order;
}
